package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener and OnSeekBarChangeListener
* it is  responsible to show coupon list
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;
import org.json.JSONException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.ws.RestClient;

public class CouponsActivity extends ListActivity implements OnClickListener, OnSeekBarChangeListener {
	private static final String tag = "CouponsActivity";

	private TextView txtCouponSearch;
	private ImageView btnCouponSearch;
	private ImageView btnDialogSort;
	private ImageView btnDialogRange;
	private Dialog dialogSort;
	private Dialog dialogRange;
	private ImageView btnDialogSortSet;
	private ImageView btnDialogRangeSet;
	private RadioGroup radioSortGroup;
	private TextView txtCouponRange;
	private SeekBar seekBarCouponRange;
	private double rangeOnDialog;
	private CheckBox checkBoxReverseSort;

	// Filter parameters
	private double range;
	private int limit;;
	private Boolean sortDistance;
	private Boolean sortStartDate;
	private Boolean sortEndDate;
	private Boolean sortCouponName;
	private Boolean sortBrandName;
	private Boolean sortRating;
	private int sort;
	private String search;
	private int brandId;

	// For Coupon List
	private ProgressDialog progressDialog;
	private ArrayList<CouponBean> coupons;
	private CouponListAdapter couponListAdapter;
	private Runnable viewCoupons;
	
	private static AlertDialog alertConnectionError;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coupons);

		// ++++++++++++ Get Initialization Parameters ++++++++++++++++
		Bundle b = getIntent().getExtras();
		range = b.getDouble("range");
		limit = b.getInt("limit");
		sortDistance = b.getBoolean("sd");
		sortStartDate = b.getBoolean("ssd");
		sortEndDate = b.getBoolean("sed");
		sortCouponName = b.getBoolean("scn");
		sortBrandName = b.getBoolean("sbn");
		sortRating = b.getBoolean("sr");
		sort = b.getInt("sort");
		search = b.getString("search");
		brandId = b.getInt("bid");

		// // ++++++++++++ Layout Initialization ++++++++++++++++
		dialogSort = new Dialog(this);
		dialogSort.setContentView(R.layout.dialog_coupons_sort);
		dialogSort.setTitle("Sort Options");
		dialogSort.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		dialogRange = new Dialog(this);
		dialogRange.setContentView(R.layout.dialog_coupons_range);
		dialogRange.setTitle("Range Options");
		dialogRange.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
		txtCouponSearch = (TextView) findViewById(R.id.txtCouponSearch);
		btnCouponSearch = (ImageView) findViewById(R.id.btnCouponSearch);
		btnCouponSearch.setOnClickListener(this);
		btnDialogSort = (ImageView) findViewById(R.id.btnDialogSort);
		btnDialogSort.setOnClickListener(this);
		btnDialogRange = (ImageView) findViewById(R.id.btnDialogRange);
		btnDialogRange.setOnClickListener(this);
		btnDialogSortSet = (ImageView) dialogSort.findViewById(R.id.btnDialogSortSet);
		btnDialogSortSet.setOnClickListener(this);
		radioSortGroup = (RadioGroup) dialogSort.findViewById(R.id.radioSortGroup);
		checkBoxReverseSort = (CheckBox) dialogSort.findViewById(R.id.checkBoxReverseSort);
		btnDialogRangeSet = (ImageView) dialogRange.findViewById(R.id.btnDialogRangeSet);
		btnDialogRangeSet.setOnClickListener(this);
		txtCouponRange = (TextView) dialogRange.findViewById(R.id.txtCouponRange);
		seekBarCouponRange = (SeekBar) dialogRange.findViewById(R.id.seekBarCouponRange);
		seekBarCouponRange.setOnSeekBarChangeListener(this);
		seekBarCouponRange.setProgress((int) (range * 10));

		alertConnectionError = new AlertDialog.Builder(this).create();
		alertConnectionError.setTitle("Error");
		alertConnectionError.setMessage("There is not Internet Connection!");
		alertConnectionError.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		
		// ++++++++++++++ Initialization Coupon List Adapter +++++++++++++
		if (Util.checkConnection(this) != null) {
			coupons = new ArrayList<CouponBean>();
			couponListAdapter = new CouponListAdapter(this, R.layout.row_coupon, coupons, tag);
			setListAdapter(couponListAdapter);
			createCouponList();
		} else {
			alertConnectionError.show();
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnCouponSearch:
			search = txtCouponSearch.getText().toString();
			createCouponList();
			break;
		case R.id.btnDialogSort:
			dialogSort.show();
			break;
		case R.id.btnDialogRange:
			dialogRange.show();
			break;
		case R.id.btnDialogSortSet:
			int selectedRadioButton = radioSortGroup.getCheckedRadioButtonId();
			sortDistance = false;
			sortStartDate = false;
			sortEndDate = false;
			sortCouponName = false;
			sortBrandName = false;
			sortRating = false;
			if(checkBoxReverseSort.isChecked())
				sort = 1;
			else 
				sort = 0;
			
			switch (selectedRadioButton) {
			case R.id.radioSortDistance:
				sortDistance = true;
				createCouponList();
				break;
			case R.id.radioSortSDate:
				sortStartDate = true;
				createCouponList();
				break;
			case R.id.radioSortEDate:
				sortEndDate = true;
				createCouponList();
				break;
			case R.id.radioSortName:
				sortCouponName = true;
				createCouponList();
				break;
			case R.id.radioSortBrand:
				sortBrandName = true;
				createCouponList();
				break;
			case R.id.radioSortRating:
				sortRating = true;
				createCouponList();
				break;
			}
			dialogSort.hide();
			break;
		case R.id.btnDialogRangeSet:
			dialogRange.hide();
			range = rangeOnDialog / 10;
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

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		rangeOnDialog = progress;
		txtCouponRange.setText("Range:" + rangeOnDialog / 10 + " km");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	// +++++++++++ Creates coupon list ++++++++++++++++++++
	public void createCouponList() {
		couponListAdapter.clear();
		viewCoupons = new Runnable() {
			@Override
			public void run() {
				try {
					coupons = RestClient.getCouponsByLocation(SessionUserBean.getLat(), SessionUserBean.getLng(), range, limit, sortDistance, sortStartDate, sortEndDate, sortCouponName, sortBrandName, sortRating, sort, search, brandId);
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
		progressDialog = ProgressDialog.show(CouponsActivity.this, "Please wait...", "Retreiving Data...", true);
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
