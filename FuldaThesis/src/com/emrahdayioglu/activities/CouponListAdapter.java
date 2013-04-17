package com.emrahdayioglu.activities;
/**
* This class modifies android ListView for coupon list
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.CouponBean;

public class CouponListAdapter extends ArrayAdapter<CouponBean> {

	private ArrayList<CouponBean> coupons;
	Context context;
	public ImageLoader imageLoader;
	private String tag;
	private int layout;

	public CouponListAdapter(Context context, int textViewResourceId, ArrayList<CouponBean> coupons, String tag) {
		super(context, textViewResourceId, coupons);
		this.coupons = coupons;
		this.context = context;
		this.tag = tag;
		if ("FavCouponsActivity".equals(tag)) {
			this.layout = R.layout.row_favcoupon;
		} else if ("MyCouponsActivity".equals(tag)) {
			this.layout = R.layout.row_mycoupon;
		} else {
			this.layout = R.layout.row_coupon;
		}

		imageLoader = new ImageLoader(context);
	}

	public int getCount() {
		return coupons.size();
	}

	public CouponBean getItem(int position) {
		return coupons.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	public static class ViewHolder {
		public TextView name;
		public TextView description;
		public ImageView image;
		public CheckBox checkBoxRow;
		public ImageView imgBaby;
		public ImageView imgPet;
		public ImageView imgPark;
		public ImageView imgHealth;
		public TextView txtDistance;
		public TextView txtDate;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(layout, null);
			holder = new ViewHolder();
			if ("FavCouponsActivity".equals(tag)) {
				holder.name = (TextView) v.findViewById(R.id.txtCouponName);
				holder.description = (TextView) v.findViewById(R.id.txtDescription);
				holder.image = (ImageView) v.findViewById(R.id.imgCoupon);
				holder.imgBaby = (ImageView) v.findViewById(R.id.imgBaby);
				holder.imgPet = (ImageView) v.findViewById(R.id.imgPet);
				holder.imgPark = (ImageView) v.findViewById(R.id.imgPark);
				holder.imgHealth = (ImageView) v.findViewById(R.id.imgHealth);
				holder.txtDistance = (TextView) v.findViewById(R.id.txtDistance);
				holder.checkBoxRow = (CheckBox) v.findViewById(R.id.checkBoxRow);
				holder.checkBoxRow.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if (coupons.get(position).isFavorited()) {
							coupons.get(position).setFavorited(false);
						} else {
							coupons.get(position).setFavorited(true);
						}
					}
				});
				
			} else if ("MyCouponsActivity".equals(tag)) {
				holder.name = (TextView) v.findViewById(R.id.txtCouponName);
				holder.description = (TextView) v.findViewById(R.id.txtDescription);
				holder.image = (ImageView) v.findViewById(R.id.imgCoupon);
				holder.txtDate = (TextView) v.findViewById(R.id.txtDate);
			} else {
				holder.name = (TextView) v.findViewById(R.id.txtCouponName);
				holder.description = (TextView) v.findViewById(R.id.txtDescription);
				holder.image = (ImageView) v.findViewById(R.id.imgCoupon);
				holder.imgBaby = (ImageView) v.findViewById(R.id.imgBaby);
				holder.imgPet = (ImageView) v.findViewById(R.id.imgPet);
				holder.imgPark = (ImageView) v.findViewById(R.id.imgPark);
				holder.imgHealth = (ImageView) v.findViewById(R.id.imgHealth);
				holder.txtDistance = (TextView) v.findViewById(R.id.txtDistance);
			}
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}

		if ("FavCouponsActivity".equals(tag)) {
			holder.name.setText(coupons.get(position).getName());
			holder.description.setText(coupons.get(position).getDescription());
			holder.image.setTag(coupons.get(position).getBrandImage());
			if (coupons.get(position).isChildcare())
				holder.imgBaby.setVisibility(View.VISIBLE);
			if (coupons.get(position).isPet())
				holder.imgPet.setVisibility(View.VISIBLE);
			if (coupons.get(position).isParkplace())
				holder.imgPark.setVisibility(View.VISIBLE);
			if (coupons.get(position).isDisabled())
				holder.imgHealth.setVisibility(View.VISIBLE);
			holder.txtDistance.setText("Distance: "+Util.formatDoubleValue(coupons.get(position).getDistance() * 1000, 0)+" meters");
			imageLoader.DisplayImage(coupons.get(position).getBrandImage(), context, holder.image);
		} else if ("MyCouponsActivity".equals(tag)) {
			holder.name.setText(coupons.get(position).getName());
			holder.description.setText(coupons.get(position).getDescription());
			holder.image.setTag(coupons.get(position).getBrandImage());
			holder.txtDate.setText("CheckIn Date: "+Util.formatDate(coupons.get(position).getCheckinDateTime()));
			imageLoader.DisplayImage(coupons.get(position).getBrandImage(), context, holder.image);
			
		} else {
			holder.name.setText(coupons.get(position).getName());
			holder.description.setText(coupons.get(position).getDescription());
			holder.image.setTag(coupons.get(position).getBrandImage());
			if (coupons.get(position).isChildcare())
				holder.imgBaby.setVisibility(View.VISIBLE);
			if (coupons.get(position).isPet())
				holder.imgPet.setVisibility(View.VISIBLE);
			if (coupons.get(position).isParkplace())
				holder.imgPark.setVisibility(View.VISIBLE);
			if (coupons.get(position).isDisabled())
				holder.imgHealth.setVisibility(View.VISIBLE);
			holder.txtDistance.setText("Distance: "+Util.formatDoubleValue(coupons.get(position).getDistance() * 1000, 0)+" meters");
			imageLoader.DisplayImage(coupons.get(position).getBrandImage(), context, holder.image);
		}
		return v;
	}

}