package com.emrahdayioglu;
/**
* This class is extended from Android Service and implement LocationListener
* The service can be binded or started.
* If it is started, its lifecycle will be finished with the destroy of application 
* If it is binded, it will work on the background 
* This class gets location from location listener through best location providers
* Location providers ,according to criterias, can be Wi-Fi , Cell ID or GPS/GLONASS
* It calls MyDB class to store user location to local database   
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;
import java.util.Iterator;

import android.app.Service;
import android.content.Intent;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.emrahdayioglu.activities.ServiceDialogActivity;
import com.emrahdayioglu.beans.NotificationBean;
import com.emrahdayioglu.beans.SatelliteBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.db.MyDB;
import com.emrahdayioglu.ws.RestClient;

public class LocationService extends Service implements LocationListener {

	private static final String tag = "LocationService";
	public final static String locationUpdate = "LocationUpdate";

	private static LocationManager locationManager;
	private final IBinder locationBinder = new LocalBinder();
	public static ArrayList<SatelliteBean> satelliteList;
	private static double lat;
	private static double lng;
	private static MyDB db;

	/**
	* extended from Android Binder class to bind service to work on background
	*/
	public class LocalBinder extends Binder {
		LocationService getService() {
			return LocationService.this;
		}
	}
	
	/**
	* Overrided from android service class
	* works on bind of service
	* @param Intent
	* @return IBinder
	*/
	@Override
	public IBinder onBind(Intent intent) {
		return locationBinder;
	}

	/**
	* Overrided from android service class 
	* works on create in service lifecycle
	*/
	@Override
	public void onCreate() {
		Toast.makeText(this, "Application getting location...", Toast.LENGTH_LONG).show();
		Log.d(tag, "Service onCreate");
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

		Criteria criteria = new Criteria();
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(false);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String providerFine = locationManager.getBestProvider(criteria, true);
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		String providerCoarse = locationManager.getBestProvider(criteria, true);

		if (providerCoarse != null) {
			locationManager.requestLocationUpdates(providerCoarse, 10000, 100, this);
		}
		if (providerFine != null) {
			locationManager.requestLocationUpdates(providerFine, 10000, 10, this);
		}
		
		MyGpsStatusListener myGpsStatusListener = new MyGpsStatusListener();
		locationManager.addGpsStatusListener(myGpsStatusListener);

		db = new MyDB(this);
		db.open();
	}

	/**
	* Overrided from android service class
	* works on destroy in service lifecycle
	*/
	@Override
	public void onDestroy() {
		Toast.makeText(this, "Location service destroyed...", Toast.LENGTH_LONG).show();
		Log.d(tag, "Service onDestroy");
		db.close();
		locationManager.removeUpdates(this);
	}

	/**
	* Overrided from android LocationListener
	* works on location changed
	*/
	@Override
	public void onLocationChanged(Location location) {
		lat = location.getLatitude();
		lng = location.getLongitude();
		SessionUserBean.setLat(lat);
		SessionUserBean.setLng(lng);
		Log.d(tag, "Update DB location with lat:" + lat + " lng:" + lng);
		db.updateLocation(SessionUserBean.getId(), lat, lng);
		Intent intent = new Intent();
		intent.setAction(locationUpdate);
		sendBroadcast(intent);

		if (SessionUserBean.getService() == 1) {
			NotificationBean notification = RestClient.sendServiceInfo(SessionUserBean.getId(), SessionUserBean.getLat(), SessionUserBean.getLng());
			if (SessionUserBean.getPush() == 1) {
				if (notification.getErrorId() == 0) {
					Intent dialog = new Intent(this, ServiceDialogActivity.class);
					dialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Bundle b = new Bundle();
					b.putInt("msg", notification.getCouponId());
					dialog.putExtras(b);
					startActivity(dialog);
				}
			}
		}

	}


	@Override
	public void onProviderDisabled(String provider) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	/**
	* Overrided from android service class
	* works on status changed of provider
	*/
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	/**
	* Overrided from android Location Listener
	* listens satellites and their status 
	*/
	public class MyGpsStatusListener implements GpsStatus.Listener {
		@Override
		public void onGpsStatusChanged(int event) {
			// TODO Auto-generated method stub
			switch (event) {
			case GpsStatus.GPS_EVENT_STARTED:
				Log.e(tag, "GPS_EVENT_STARTED");
				break;
			case GpsStatus.GPS_EVENT_FIRST_FIX:
				Log.e(tag, "GPS_EVENT_FIRST_FIX");
				break;
			case GpsStatus.GPS_EVENT_STOPPED:
				Log.e(tag, "GPS_EVENT_STOPPED");
				break;
			case GpsStatus.GPS_EVENT_SATELLITE_STATUS:
				Log.e(tag, "GPS_EVENT_SATELLITE_STATUS");
				GpsStatus gpsStatus = locationManager.getGpsStatus(null);
				SessionUserBean.setTimeToFix(gpsStatus.getTimeToFirstFix());
				Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
				Iterator<GpsSatellite> iteratorSatellites = satellites.iterator();
				satelliteList = new ArrayList<SatelliteBean>();
				while (iteratorSatellites.hasNext()) {
					GpsSatellite satellite = iteratorSatellites.next();
					if (satellite.toString() != null) {
						if (satelliteList != null) {
								SatelliteBean sb = new SatelliteBean();
								sb.setPrn(satellite.getPrn());
								sb.setAzimuth(satellite.getAzimuth());
								sb.setElevation(satellite.getElevation());
								sb.setHasAlmanac(satellite.hasAlmanac());
								sb.setHasEphermis(satellite.hasEphemeris());
								sb.setSnr(satellite.getSnr());
								sb.setUsedInFix(satellite.usedInFix());
								satelliteList.add(sb);
						}
					}
				}
				break;
			}
		}
	}

}
