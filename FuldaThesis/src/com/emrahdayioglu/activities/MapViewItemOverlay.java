package com.emrahdayioglu.activities;
/**
* This class is extended from Android ItemizedOverlay
* It places coupon items as overlay on the map
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class MapViewItemOverlay extends ItemizedOverlay<OverlayItem> {

	private ArrayList<OverlayItem> mapOverlays = new ArrayList<OverlayItem>();

	private Context context;

	public MapViewItemOverlay(Drawable defaultMarker) {
		super(boundCenterBottom(defaultMarker));
	}

	public MapViewItemOverlay(Drawable defaultMarker, Context context) {
		this(defaultMarker);
		this.context = context;
	}
	
	@Override
	protected OverlayItem createItem(int i) {
		return mapOverlays.get(i);
	}

	@Override
	public int size() {
		return mapOverlays.size();
	}

	@Override
	protected boolean onTap(int index) {
		Intent intent = new Intent(context, CouponActivity.class);
		Bundle b = new Bundle();
		b.putInt("id", Integer.parseInt(mapOverlays.get(index).getTitle()));
		intent.putExtras(b);
		context.startActivity(intent);
		return true;
	}

	public void addOverlay(OverlayItem overlay) {
		mapOverlays.add(overlay);
		this.populate();
	}

}