package com.emrahdayioglu.activities;
/**
* This class is extended from Android ListActivity and implement OnClickListener
* it is  responsible to show satellite list
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import com.emrahdayioglu.LocationService;
import com.emrahdayioglu.R;
import com.emrahdayioglu.beans.SatelliteBean;
import com.emrahdayioglu.beans.SessionUserBean;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.TextView;

public class SatellitesActivity extends ListActivity {

	private ProgressDialog progressDialog;
	private ArrayList<SatelliteBean> satellites;
	private SatelliteListAdapter satelliteListAdapter;
	private Runnable viewCoupons;

	private TextView txtTimeToFix;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.satellites);

		satellites = new ArrayList<SatelliteBean>();
		satelliteListAdapter = new SatelliteListAdapter(this, R.layout.row_coupon, satellites);
		setListAdapter(satelliteListAdapter);
		createSatelliteList();

		txtTimeToFix = (TextView) findViewById(R.id.txtTimeToFix);
		if (SessionUserBean.getTimeToFix() != 0) {
			int timeToFirstFix = SessionUserBean.getTimeToFix();
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(timeToFirstFix);
			txtTimeToFix.setText("Time To First Fix:" + formatter.format(calendar.getTime()));
		}

	}

	// +++++++++++ Creates coupon list ++++++++++++++++++++
	public void createSatelliteList() {
		satelliteListAdapter.clear();
		viewCoupons = new Runnable() {
			@Override
			public void run() {
				satellites = LocationService.satelliteList;
				runOnUiThread(returnRes);
			}
		};
		Thread thread = new Thread(null, viewCoupons, "CouponListBackgroundThread");
		thread.start();
		progressDialog = ProgressDialog.show(SatellitesActivity.this, "Please wait...", "Retreiving Data...", true);
	}

	private Runnable returnRes = new Runnable() {
		@Override
		public void run() {
			if (satellites != null && satellites.size() > 0) {
				satelliteListAdapter.notifyDataSetChanged();
				for (int i = 0; i < satellites.size(); i++)
					satelliteListAdapter.add(satellites.get(i));
			}
			progressDialog.dismiss();
			satelliteListAdapter.notifyDataSetChanged();
		}
	};

}
