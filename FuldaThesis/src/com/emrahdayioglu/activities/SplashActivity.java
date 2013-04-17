package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity
* first screen when application loaded
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import com.emrahdayioglu.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashActivity extends Activity {
	private static final String tag = "SplashActivity";

	private static final int stopSplash = 0;
	private static final long splashTime = 1000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		Log.d(tag, "splash created");

		Message msg = new Message();
		msg.what = stopSplash;
		splashHandler.sendMessageDelayed(msg, splashTime);
	}

	private Handler splashHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case stopSplash:
				Log.d(tag, "splash timed out!");
				Intent intent = new Intent(getApplicationContext(),	AuthActivity.class);
				startActivity(intent);
				break;
			}
			super.handleMessage(msg);
		}
	};

	
}