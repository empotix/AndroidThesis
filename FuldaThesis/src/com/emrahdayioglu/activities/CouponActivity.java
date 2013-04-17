package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener and OnSeekBarChangeListener
* Is responsible to show coupon informaton
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.emrahdayioglu.Errors;
import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.ws.RestClient;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class CouponActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private static final String tag = "CouponActivity";

	CouponBean cb;

	private ImageView imgCoupon;
	private TextView txtBrandName;
	private TextView txtTitle;
	private TextView txtDescription;
	private SeekBar seekBarCheckIn;

	private RelativeLayout btnEndDate;
	private RelativeLayout btnFav;
	private RelativeLayout btnLocation;
	private RelativeLayout btnUsage;
	private RelativeLayout btnInfo;
	private RelativeLayout btnTerms;
	
	
	private TextView txtEndDate;
	private TextView txtEndDate2;
	private TextView txtFav;
	private ImageView imgFavOn;
	private ImageView imgFavOff;
	private TextView txtLocation;
	private TextView txtLocation2;
	private TextView txtUsage;
	private TextView txtDialogUsage;
	private TextView txtDialogInfo;
	private TextView txtDialogCouponCode;
	private TextView txtCouponCodeTitle;
	private TextView txtCouponCode;
	private LinearLayout linearLayoutCheckIn;
	
	private Dialog dialogDate;
	private Dialog dialogLocation;
	private Dialog dialogUsage;
	private Dialog dialogInfo;
	private Dialog dialogTerms;
	private Dialog dialogCheckIn;
	

	int couponId;
	String brandName;
	String branchAddress; 
	String couponLat;
	String couponLng;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupon);

		 Bundle b = getIntent().getExtras();
		 couponId = b.getInt("id");

		imgCoupon = (ImageView) findViewById(R.id.imgCoupon);
		txtBrandName = (TextView) findViewById(R.id.txtBrandName);
		txtTitle = (TextView) findViewById(R.id.txtTitle);
		txtDescription = (TextView) findViewById(R.id.txtDescription);
		seekBarCheckIn = (SeekBar) findViewById(R.id.seekBarCheckIn);
		seekBarCheckIn.setOnSeekBarChangeListener(this);
		btnEndDate = (RelativeLayout) findViewById(R.id.btnEndDate);
		btnEndDate.setOnClickListener(this);
		btnFav = (RelativeLayout) findViewById(R.id.btnFav);
		btnFav.setOnClickListener(this);
		btnLocation = (RelativeLayout) findViewById(R.id.btnLocation);
		btnLocation.setOnClickListener(this);
		btnUsage = (RelativeLayout) findViewById(R.id.btnUsage);
		btnUsage.setOnClickListener(this);
		btnInfo = (RelativeLayout) findViewById(R.id.btnInfo);
		btnInfo.setOnClickListener(this);
		btnTerms = (RelativeLayout) findViewById(R.id.btnTerms);
		btnTerms.setOnClickListener(this);
		
		txtEndDate = (TextView) findViewById(R.id.txtEndDate);
		txtEndDate2 = (TextView) findViewById(R.id.txtEndDate2);
		txtFav = (TextView) findViewById(R.id.txtFav);
		imgFavOn = (ImageView) findViewById(R.id.imgFavOn);
		imgFavOff = (ImageView) findViewById(R.id.imgFavOff);
		txtLocation = (TextView) findViewById(R.id.txtLocation);
		txtLocation2 = (TextView) findViewById(R.id.txtLocation2);
		txtUsage = (TextView) findViewById(R.id.txtUsage);
		linearLayoutCheckIn = (LinearLayout) findViewById(R.id.linearLayoutCheckIn);
		txtCouponCodeTitle = (TextView) findViewById(R.id.txtCouponCodeTitle);
		txtCouponCode = (TextView) findViewById(R.id.txtCouponCode);
		
		
		dialogDate = new Dialog(this);
		dialogDate.setContentView(R.layout.dialog_coupon_date);
		dialogDate.setTitle("Dates");
		dialogDate.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		dialogLocation = new Dialog(this);
		dialogLocation.setContentView(R.layout.dialog_coupon_location);
		dialogLocation.setTitle("Location");
		dialogLocation.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		dialogUsage = new Dialog(this);
		dialogUsage.setContentView(R.layout.dialog_coupon_usage);
		dialogUsage.setTitle("Usage");
		dialogUsage.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		dialogInfo = new Dialog(this);
		dialogInfo.setContentView(R.layout.dialog_coupon_info);
		dialogInfo.setTitle("Information");
		dialogInfo.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		dialogTerms = new Dialog(this);
		dialogTerms.setContentView(R.layout.dialog_coupon_terms);
		dialogTerms.setTitle("Terms and Conditions");
		dialogTerms.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		dialogCheckIn = new Dialog(this);
		dialogCheckIn.setContentView(R.layout.dialog_coupon_checkin);
		dialogCheckIn.setTitle("Check In");
		dialogCheckIn.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		
		if (couponId > 0) {
			cb = RestClient.getCoupon(couponId, SessionUserBean.getId(), SessionUserBean.getLat(), SessionUserBean.getLng());

			URL url;
			try {
				url = new URL(cb.getImage());
				brandName = cb.getBrandName();
				branchAddress = cb.getBranchAddress();
				couponLat = ""+cb.getLat();
				couponLng = ""+cb.getLng();
				InputStream content = (InputStream) url.getContent();
				Drawable drawable = Drawable.createFromStream(content, "src");
				imgCoupon.setImageDrawable(drawable);
				txtBrandName.setText(cb.getBrandName());
				txtTitle.setText(cb.getName() + " in " + cb.getBranchName());
				txtDescription.setText(cb.getDescription());
				if(cb.getCheckinDateTime() != null){
					txtEndDate2.setText("Checked In");
					txtEndDate.setText(Util.formatDate(cb.getCheckinDateTime()));
					seekBarCheckIn.setVisibility(View.INVISIBLE);
					linearLayoutCheckIn.setVisibility(View.GONE);
					txtCouponCodeTitle.setVisibility(View.VISIBLE);
					txtCouponCode.setVisibility(View.VISIBLE);
					txtCouponCode.setText(cb.getCouponCode());
					
				} else {
					txtEndDate.setText(Util.formatDate(cb.getEndDateTime()));
				}
				txtFav.setText(cb.getFavCouponCount()+" times favorited");
				if(cb.isFavorited()){
					imgFavOn.setVisibility(View.VISIBLE);
					imgFavOff.setVisibility(View.INVISIBLE);
				} else {
					imgFavOn.setVisibility(View.INVISIBLE);
					imgFavOff.setVisibility(View.VISIBLE);
				}

				txtLocation.setText(Util.formatDoubleValue(cb.getDistance() * 1000, 0)+" meters");
				txtLocation2.setText(cb.getBranchDistrict());
				txtUsage.setText(""+cb.getUserCouponCount());
				TextView txtLocationDialogAddress = (TextView) dialogLocation.findViewById(R.id.txtAddress);
				txtLocationDialogAddress.setText(cb.getBranchAddress());
				TextView txtLocationDialogDistance = (TextView) dialogLocation.findViewById(R.id.txtDistance);
				txtLocationDialogDistance.setText(Util.formatDoubleValue(cb.getDistance() * 1000, 0)+" meters");
				TextView txtLocationDialogLatLng = (TextView) dialogLocation.findViewById(R.id.txtLatLng);
				txtLocationDialogLatLng.setText("Latitude:"+cb.getLat()+" Longitude:"+cb.getLng());
				txtDialogUsage = (TextView) dialogUsage.findViewById(R.id.txtDialogUsage);
				txtDialogUsage.setText(""+cb.getUserCouponCount());
				txtDialogInfo = (TextView) dialogInfo.findViewById(R.id.txtDialogInfo);
				txtDialogInfo.setText(cb.getDetail());
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnEndDate:
			dialogDate.show();
			break;
		case R.id.btnFav:
			if(cb.isFavorited()){
				CouponBean favCoupon = RestClient.deleteFavCoupons(SessionUserBean.getId(), ""+cb.getId());
				if (favCoupon.getErrorId() == 0) {
					cb.setFavorited(false);
					imgFavOn.setVisibility(View.INVISIBLE);
					imgFavOff.setVisibility(View.VISIBLE);
				} else {
					Toast.makeText(CouponActivity.this, "Error!! "+favCoupon.getErrorMessage(), Toast.LENGTH_LONG).show();
				}
			} else {
				CouponBean favCoupon = RestClient.addMyFavorites(couponId, SessionUserBean.getId());
				if (favCoupon.getErrorId() == 0) {
					cb.setFavorited(true);
					imgFavOn.setVisibility(View.VISIBLE);
					imgFavOff.setVisibility(View.INVISIBLE);
				} else {
					Toast.makeText(CouponActivity.this, "Error!! "+favCoupon.getErrorMessage(), Toast.LENGTH_LONG).show();
				}
			}
			break;
		case R.id.btnLocation:
			dialogLocation.show();
			break;
		case R.id.btnUsage:
			dialogUsage.show();
			break;
		case R.id.btnInfo:
			dialogInfo.show();
			break;
		case R.id.btnTerms:
			dialogTerms.show();
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		if (seekBarCheckIn.getProgress() > 80) {
			seekBarCheckIn.setProgress(100);
			CouponBean checkinCoupon = RestClient.getCouponCode(couponId, SessionUserBean.getId(), SessionUserBean.getLat(), SessionUserBean.getLng());
			txtDialogCouponCode = (TextView) dialogCheckIn.findViewById(R.id.txtCouponCode);
			if (checkinCoupon.getErrorId() == 0){
				txtDialogCouponCode.setText(checkinCoupon.getCouponCode());
				seekBarCheckIn.setVisibility(View.INVISIBLE);
				linearLayoutCheckIn.setVisibility(View.GONE);
				txtCouponCodeTitle.setVisibility(View.VISIBLE);
				txtCouponCode.setVisibility(View.VISIBLE);
				txtCouponCode.setText(checkinCoupon.getCouponCode());
			} else if (checkinCoupon.getErrorId() == Errors.ERROR_ID_DUPLICATE_CHECKIN){	
				txtDialogCouponCode.setText("Error during checkin coupon");
				seekBarCheckIn.setProgress(0);
			} else if (checkinCoupon.getErrorId() == Errors.ERROR_ID_WRONG_LOCATION){
				txtDialogCouponCode.setText("You can not check in to this coupon in this location. For check in you have to be in "+brandName+" from "+branchAddress+" in latitude "+couponLat+" and longitude "+couponLng);
				seekBarCheckIn.setProgress(0);
			}
			
			dialogCheckIn.show();
		} else {
			seekBarCheckIn.setProgress(0);
		}
	}

}
