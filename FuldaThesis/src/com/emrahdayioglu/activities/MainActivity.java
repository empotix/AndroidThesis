package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener
* Is responsible to show main menu
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.emrahdayioglu.LocationService;
import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.SessionUserBean;

public class MainActivity extends Activity implements OnClickListener {

	private static final String tag = "MainActivity";

	private ImageView btnCoupons;
	private ImageView btnFavorites;
	private ImageView btnHistory;
	private ImageView btnBrands;
	private ImageView btnMap;
	private TextView txtLocation;
	private TextView txtAddress;
	private ImageView btnSettings;

	private static Intent serviceIntent;
	LocationBroadcastReceiver locationBroadcastReceiver;
	Geocoder geocoder;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		// Listens location changes from service
		locationBroadcastReceiver = new LocationBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(LocationService.locationUpdate);
		registerReceiver(locationBroadcastReceiver, intentFilter);

		// Binds or Starts location service
		serviceIntent = new Intent(this, LocationService.class);
		if (SessionUserBean.getService() == 0) {
			this.bindService(serviceIntent, locationServiceConn, Context.BIND_AUTO_CREATE);
		}
		this.startService(serviceIntent);

		btnCoupons = (ImageView) findViewById(R.id.btnCoupons);
		btnCoupons.setOnClickListener(this);
		btnFavorites = (ImageView) findViewById(R.id.btnFavorites);
		btnFavorites.setOnClickListener(this);
		btnHistory = (ImageView) findViewById(R.id.btnHistory);
		btnHistory.setOnClickListener(this);
		btnBrands = (ImageView) findViewById(R.id.btnBrands);
		btnBrands.setOnClickListener(this);
		btnMap = (ImageView) findViewById(R.id.btnMap);
		btnMap.setOnClickListener(this);
		txtLocation = (TextView) findViewById(R.id.txtLocation);
		txtAddress = (TextView) findViewById(R.id.txtAddress);
		btnSettings = (ImageView) findViewById(R.id.btnSettings);
		btnSettings.setOnClickListener(this);

		geocoder = new Geocoder(this);
		updateLocationFields();
	}

	@Override
	protected void onResume() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// Destroy service if it is not allowed on the background
		this.stopService(new Intent(this, LocationService.class));
		if (SessionUserBean.getService() == 0) {
			this.unbindService(locationServiceConn);
		}
	}
	
	@Override
	public void onClick(View v) {
		final Intent intent;
		Bundle b;
		switch (v.getId()) {
		case R.id.btnCoupons:
			intent = new Intent(this, CouponsActivity.class);
			b = new Bundle();
			b.putDouble("range", SessionUserBean.getRange());
			b.putInt("limit", 20);
			b.putBoolean("sd", true);
			b.putBoolean("ssd", false);
			b.putBoolean("sed", false);
			b.putBoolean("scn", false);
			b.putBoolean("sbn", false);
			b.putBoolean("sr", false);
			b.putInt("sort", 0);
			b.putString("search", "");
			intent.putExtras(b);
			startActivity(intent);
			break;
		case R.id.btnFavorites:
			intent = new Intent(this, FavCouponsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnHistory:
			intent = new Intent(this, MyCouponsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnBrands:
			intent = new Intent(this, BrandsActivity.class);
			startActivity(intent);
			break;
		case R.id.btnMap:
			intent = new Intent(this, MapViewActivity.class);
			startActivity(intent);
			break;
		case R.id.btnSettings:
			intent = new Intent(this, SettingsActivity.class);
			startActivity(intent);
			break;
		}

	}


	private class LocationBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateLocationFields();
		}

	}

	private void updateLocationFields() {
		txtLocation.setText("Latitude:" + Util.formatDoubleValue(SessionUserBean.getLat(), 4) + " Longitude:" + Util.formatDoubleValue(SessionUserBean.getLng(), 4));
		List<Address> addresses;
		try {
			addresses = geocoder.getFromLocation(SessionUserBean.getLat(), SessionUserBean.getLng(), 10);
			for (Address address : addresses) {
				txtAddress.setText(address.getAddressLine(0) + " " + address.getAddressLine(1));
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	protected static ServiceConnection locationServiceConn = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

	};
	
	
}
