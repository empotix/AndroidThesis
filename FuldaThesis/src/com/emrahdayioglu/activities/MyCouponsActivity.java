package com.emrahdayioglu.activities;
/**
* This class is extended from Android ListActivity and implement OnClickListener
* it is  responsible to show used coupon list
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MyCouponsActivity extends ListActivity implements OnClickListener{
	private static final String tag = "MyCouponsActivity";

	private TextView txtCouponSearch;
	private ImageView btnCouponSearch;
	private String search = "";

	// For Coupon List
	private ProgressDialog progressDialog;
	private ArrayList<CouponBean> coupons;
	private CouponListAdapter couponListAdapter;
	private Runnable viewCoupons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycoupons);

		// ++++++++++++ Layout Initialization ++++++++++++++++
		txtCouponSearch = (TextView) findViewById(R.id.txtCouponSearch);
		btnCouponSearch = (ImageView) findViewById(R.id.btnCouponSearch);
		btnCouponSearch.setOnClickListener(this);
		
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
					coupons = RestClient.getMyCoupons(SessionUserBean.getId(), search);
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
		progressDialog = ProgressDialog.show(MyCouponsActivity.this, "Please wait...", "Retreiving Data...", true);
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


}
