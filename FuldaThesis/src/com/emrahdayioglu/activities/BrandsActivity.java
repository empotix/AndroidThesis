package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener
* Is responsible to show brand list
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;
import org.json.JSONException;
import com.emrahdayioglu.R;
import com.emrahdayioglu.beans.BrandBean;
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

public class BrandsActivity extends ListActivity implements OnClickListener{
	private static final String tag = "BrandsActivity";
	
	private TextView txtCouponSearch;
	private ImageView btnCouponSearch;
	private String search = "";

	// For Coupon List
	private ProgressDialog progressDialog;
	private ArrayList<BrandBean> brands;
	private BrandListAdapter brandListAdapter;
	private Runnable viewCoupons;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.brands);

		// Layout Initialization 
		txtCouponSearch = (TextView) findViewById(R.id.txtCouponSearch);
		btnCouponSearch = (ImageView) findViewById(R.id.btnCouponSearch);
		btnCouponSearch.setOnClickListener(this);
		
		// Initialization Coupon List Adapter 
		brands = new ArrayList<BrandBean>();
		brandListAdapter = new BrandListAdapter(this, R.layout.row_coupon, brands);
		setListAdapter(brandListAdapter);
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
		Intent intent = new Intent(this, CouponsActivity.class);
		Bundle b = new Bundle();
		b.putDouble("range", SessionUserBean.getRange());
		b.putInt("limit", 100);
		b.putBoolean("sd", true);
		b.putBoolean("ssd", false);
		b.putBoolean("sed", false);
		b.putBoolean("scn", false);
		b.putBoolean("sbn", false);
		b.putBoolean("sr", false);
		b.putInt("sort", 1);
		b.putString("search", "");
		b.putInt("bid", brands.get(position).getId());
		intent.putExtras(b);
		startActivity(intent);
	}


	// Creates coupon list 
	public void createCouponList() {
		brandListAdapter.clear();
		viewCoupons = new Runnable() {
			@Override
			public void run() {
				try {
					brands = RestClient.getBrands(search);
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
		progressDialog = ProgressDialog.show(BrandsActivity.this, "Please wait...", "Retreiving Data...", true);
	}

	// Ads Coupon object received from WS and adds to list
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (brands != null && brands.size() > 0) {
				brandListAdapter.notifyDataSetChanged();
				for (int i = 0; i < brands.size(); i++)
					brandListAdapter.add(brands.get(i));
			}
			progressDialog.dismiss();
			brandListAdapter.notifyDataSetChanged();
		}
	};


}
