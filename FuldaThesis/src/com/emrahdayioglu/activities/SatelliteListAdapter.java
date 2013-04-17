package com.emrahdayioglu.activities;
/**
* This class is extended from Android ListActivity and implement OnClickListener
* it is  responsible to show satellite list
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
import com.emrahdayioglu.beans.SatelliteBean;

public class SatelliteListAdapter extends ArrayAdapter<SatelliteBean> {
	
	private ArrayList<SatelliteBean> satellites;
	Context context;
	public ImageLoader imageLoader;

	public SatelliteListAdapter(Context context, int textViewResourceId, ArrayList<SatelliteBean> satellites) {
		super(context, textViewResourceId, satellites);
		this.satellites = satellites;
		this.context = context;
		imageLoader = new ImageLoader(context);
	}
	
	public int getCount() {
        return satellites.size();
    }

    public SatelliteBean getItem(int position) {
        return satellites.get(position);
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder{
        public TextView txtPrn;
        public ImageView image;
        public TextView txtAzimuth;
        public TextView txtElevation;
        public TextView txtSnr;
        public TextView txtAlmanac;
        public TextView txtEphermis;
        public TextView txtUsedInFix;
        public TextView txtSatType;
        
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder = null;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.row_satellite, null);
            holder=new ViewHolder();
            holder.txtPrn=(TextView)v.findViewById(R.id.txtPrn);
            holder.image=(ImageView)v.findViewById(R.id.imgCoupon);
            holder.txtAzimuth = (TextView) v.findViewById(R.id.txtAzimuth);
            holder.txtElevation = (TextView) v.findViewById(R.id.txtElevation);
            holder.txtSnr = (TextView) v.findViewById(R.id.txtSnr);
            holder.txtAlmanac = (TextView) v.findViewById(R.id.txtAlmanac);
            holder.txtEphermis = (TextView) v.findViewById(R.id.txtEphermis);
            holder.txtUsedInFix = (TextView) v.findViewById(R.id.txtUsedInFix);
            holder.txtSatType = (TextView) v.findViewById(R.id.txtSatType);
            v.setTag(holder);
		} else {
			holder=(ViewHolder)v.getTag();
		}
		
		holder.txtPrn.setText("Prn: "+satellites.get(position).getPrn());
        //holder.image.setTag(satellites.get(position).getImage());
        holder.txtAzimuth.setText("Azi: "+satellites.get(position).getAzimuth());
        holder.txtElevation.setText("Elv: "+satellites.get(position).getElevation());
        holder.txtSnr.setText("Snr: "+satellites.get(position).getSnr());
        holder.txtAlmanac.setText("Alm: "+satellites.get(position).isHasAlmanac());
        holder.txtEphermis.setText("Eph: "+satellites.get(position).isHasEphermis());
        holder.txtUsedInFix.setText("IsFixed: "+satellites.get(position).isUsedInFix());
        if (satellites.get(position).getPrn() > 0 & satellites.get(position).getPrn() < 33){
        	holder.txtSatType.setText("GPS");
        } else if (satellites.get(position).getPrn() > 64 & satellites.get(position).getPrn() < 89){
        	holder.txtSatType.setText("GLONASS");
        }
        //imageLoader.DisplayImage(satellites.get(position).getImage(), context, holder.image);
        
		return v;
	}

}
