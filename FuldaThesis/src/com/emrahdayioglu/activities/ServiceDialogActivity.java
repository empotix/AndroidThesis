package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener
* it is  responsible to show catched coupon by the service
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.ws.RestClient;

public class ServiceDialogActivity extends Activity implements OnClickListener{

	int msg;
	TextView txtBrandName;
	TextView txtMessage;
	CouponBean cb;
	LinearLayout layoutServiceDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_service);
		Bundle b = getIntent().getExtras();
		msg = b.getInt("msg");
		cb = RestClient.getCoupon(msg, SessionUserBean.getId(), SessionUserBean.getLat(), SessionUserBean.getLng());
		txtBrandName = (TextView) findViewById(R.id.txtBrandName);
		txtBrandName.setText(cb.getBrandName());
		txtMessage = (TextView) findViewById(R.id.txtMessage);
		txtMessage.setText("'"+cb.getName()+"' is "+ Util.formatDoubleValue(cb.getDistance() * 1000, 0) + " meters away from you !");
		layoutServiceDialog = (LinearLayout) findViewById(R.id.layoutServiceDialog);
		layoutServiceDialog.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		final Intent intent;
		Bundle b;
		switch (v.getId()) {
		case R.id.layoutServiceDialog:
			intent = new Intent(this, CouponActivity.class);
			b = new Bundle();
			b.putInt("id", msg);
			intent.putExtras(b);
			startActivity(intent);
			break;
		}
	}
}
