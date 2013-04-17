package com.emrahdayioglu.activities;
/**
* This class is extended from Android MapActivity
* It is responsible to show coupons on map
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.emrahdayioglu.R;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.ws.RestClient;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.MyLocationOverlay;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class MapViewActivity extends MapActivity {

	double lat = SessionUserBean.getLat();
	double lng = SessionUserBean.getLng();

	MapView mapView;
	MapController mapController;
	MyLocationOverlay myLocationOverlay;

	private ArrayList<CouponBean> coupons;
	private List<Overlay> mapOverlays;
	private Drawable defaultItemDrawable;
	private MapViewItemOverlay mapViewItemOverlay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mapview);

		getCouponList();

		mapView = (MapView) findViewById(R.id.myMapView);
		mapView.setBuiltInZoomControls(true);
		mapController = mapView.getController();
		mapController.setZoom(14);
		

		// focusing to location
		mapController.animateTo(latLngToGeoPoint(lat, lng));

	}

	@Override
	protected void onResume() {
		super.onResume();
		mapOverlays = mapView.getOverlays();
		mapOverlays.clear();
		mapView.postInvalidate();
		mapView.removeAllViews();
		myLocationOverlay = new MyLocationOverlay(this, mapView);
		myLocationOverlay.enableMyLocation();
		mapView.getOverlays().add(myLocationOverlay);
		mapView.postInvalidate();
		defaultItemDrawable = this.getResources().getDrawable(R.drawable.ic_launcher);
		defaultItemDrawable.setBounds(-defaultItemDrawable.getIntrinsicWidth() / 2, -defaultItemDrawable.getIntrinsicHeight(), defaultItemDrawable.getIntrinsicWidth() / 2, 0);
		mapViewItemOverlay = new MapViewItemOverlay(defaultItemDrawable, this);
		if (coupons != null && !coupons.isEmpty()) {
			for (CouponBean cb : coupons) {
				//TODO burada itemized overlay icine couponId title icinde gonderiliyor, OverlayItem constructer degistirilip de gonderilebiliyormus, daha saglam
				OverlayItem overlayitem = new OverlayItem(latLngToGeoPoint(cb.getLat(), cb.getLng()), ""+cb.getId(), "Snippet "); 
				mapViewItemOverlay.addOverlay(overlayitem);
				mapOverlays.add(mapViewItemOverlay);
				MapViewItemOverlayAsync async = new MapViewItemOverlayAsync(overlayitem, mapView);
				async.execute(cb.getBrandImage());

			}
		}

	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	

	public void getCouponList() {

		try {
			coupons = RestClient.getCouponsByLocation(SessionUserBean.getLat(), SessionUserBean.getLng(), 10, 30, true, false, false, false, false, false, 0, "", 0);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private GeoPoint latLngToGeoPoint(double lat, double lng) {
		Double geoLat = lat * 1E6;
		Double geoLng = lng * 1E6;
		GeoPoint point = new GeoPoint(geoLat.intValue(), geoLng.intValue());
		return point;
	}

}
