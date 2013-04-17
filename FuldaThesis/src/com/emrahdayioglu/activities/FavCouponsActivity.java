package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener and OnSeekBarChangeListener
* Is responsible to show favorited coupon list
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;
import org.json.JSONException;
import com.emrahdayioglu.R;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.ws.RestClient;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class FavCouponsActivity extends ListActivity implements OnClickListener {
	private static final String tag = "FavCouponsActivity";

	private TextView txtCouponSearch;
	private ImageView btnCouponSearch;
	private String search = "";
	private Button btnDelete;
	private ProgressDialog progressDialog;
	private ArrayList<CouponBean> coupons;
	private CouponListAdapter couponListAdapter;
	private Runnable viewCoupons;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favcoupons);

		// ++++++++++++ Layout Initialization ++++++++++++++++
		txtCouponSearch = (TextView) findViewById(R.id.txtCouponSearch);
		btnCouponSearch = (ImageView) findViewById(R.id.btnCouponSearch);
		btnCouponSearch.setOnClickListener(this);
		btnDelete = (Button) findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(this);

		// ++++++++++++++ Initialization Coupon List Adapter +++++++++++++
		coupons = new ArrayList<CouponBean>();
		couponListAdapter = new CouponListAdapter(this, R.layout.row_coupon, coupons, tag);
		setListAdapter(couponListAdapter);
		createCouponList();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCouponSearch:
			search = txtCouponSearch.getText().toString();
			createCouponList();
			break;
		case R.id.btnDelete:
			CouponBean favCoupon = RestClient.deleteFavCoupons(SessionUserBean.getId(), getCheckedItems());
			if (favCoupon.getErrorId() == 0) {
				createCouponList();
			} else {
				Toast.makeText(FavCouponsActivity.this, "Error!! "+favCoupon.getErrorMessage(), Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, CouponActivity.class);
		Bundle b = new Bundle();
		b.putInt("id", coupons.get(position).getId());
		intent.putExtras(b);
		startActivity(intent);
	}

	// +++++++++++ Creates coupon list ++++++++++++++++++++
	public void createCouponList() {
		couponListAdapter.clear();
		viewCoupons = new Runnable() {
			@Override
			public void run() {
				try {
					coupons = RestClient.getFavCoupons(SessionUserBean.getId(), search);
					Thread.sleep(500);
					runOnUiThread(returnRes);
				} catch (JSONException e) {
					progressDialog.dismiss();
				} catch (InterruptedException e) {
					progressDialog.dismiss();
				}
			}
		};
		Thread thread = new Thread(null, viewCoupons, "CouponListBackgroundThread");
		thread.start();
		progressDialog = ProgressDialog.show(FavCouponsActivity.this, "Please wait...", "Retreiving Data...", true);
	}

	// +++++++++++++ Ads Coupon object received from WS and adds to list
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (coupons != null && coupons.size() > 0) {
				couponListAdapter.notifyDataSetChanged();
				for (int i = 0; i < coupons.size(); i++)
					couponListAdapter.add(coupons.get(i));
			}
			progressDialog.dismiss();
			couponListAdapter.notifyDataSetChanged();
		}

	};

	private String getCheckedItems() {
		String checkedItems = "";
		int count = couponListAdapter.getCount();
		for (int i = 0; i < count; i++) {
			if (!couponListAdapter.getItem(i).isFavorited()) {
				if (checkedItems.length() > 0) {
					checkedItems += "," + couponListAdapter.getItem(i).getId();
				} else {
					checkedItems += couponListAdapter.getItem(i).getId();
				}
			}
		}
		
		return checkedItems;
	}

}
