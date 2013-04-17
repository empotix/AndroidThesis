package com.emrahdayioglu.activities;
/**
* This class modifies android ListView
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
import android.widget.ImageView;
import android.widget.TextView;
import com.emrahdayioglu.R;
import com.emrahdayioglu.beans.BrandBean;

public class BrandListAdapter extends ArrayAdapter<BrandBean> {

	private ArrayList<BrandBean> brands;
	private Context context;
	public ImageLoader imageLoader;

	public BrandListAdapter(Context context, int textViewResourceId, ArrayList<BrandBean> brands) {
		super(context, textViewResourceId, brands);
		this.brands = brands;
		this.context = context;
		imageLoader = new ImageLoader(context);
	}
	
	public int getCount() {
        return brands.size();
    }

    public BrandBean getItem(int position) {
        return brands.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder{
        public TextView text;
        public ImageView image;
        public TextView description;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_brand, null);
            holder=new ViewHolder();
            holder.text=(TextView)v.findViewById(R.id.txtBrandName);
            holder.image=(ImageView)v.findViewById(R.id.imgCoupon);
            holder.description = (TextView) v.findViewById(R.id.txtDescription);
            v.setTag(holder);
		} else {
			holder=(ViewHolder)v.getTag();
		}
		holder.text.setText(brands.get(position).getName());
        holder.image.setTag(brands.get(position).getImage());
        holder.description.setText(brands.get(position).getDescription());
        imageLoader.DisplayImage(brands.get(position).getImage(), context, holder.image);
		return v;
	}
}