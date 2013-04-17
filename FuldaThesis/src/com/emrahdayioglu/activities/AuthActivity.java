package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener
* Is responsible to authorization of the user when application started  
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import com.emrahdayioglu.R;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.Constants;
import com.emrahdayioglu.db.MyDB;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AuthActivity extends Activity implements OnClickListener {

	private static final String tag = "AuthActivity";
	private static MyDB db;
	private Button btnAuthSignUp;
	private Button btnAuthFBLogin;
	private Button btnAuthLogin;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d(tag, "receiving from database");
		db = new MyDB(this);
		db.open();
		Cursor c = db.getUsers();
		startManagingCursor(c);
		if (c.moveToFirst()) {
			Log.d(tag, "data received from local db, setting sessionUserBean for id:" + c.getInt(c.getColumnIndex(Constants.KEY_ID)));
			SessionUserBean.setId(c.getInt(c.getColumnIndex(Constants.KEY_ID)));
			SessionUserBean.setTokenId(c.getString(c.getColumnIndex(Constants.TOKEN_ID)));
			SessionUserBean.setEmail(c.getString(c.getColumnIndex(Constants.EMAIL)));
			SessionUserBean.setName(c.getString(c.getColumnIndex(Constants.NAME)));
			SessionUserBean.setSurname(c.getString(c.getColumnIndex(Constants.SURNAME)));
			SessionUserBean.setBirthday(c.getString(c.getColumnIndex(Constants.BIRTHDAY)));
			SessionUserBean.setPhoneNumber(c.getString(c.getColumnIndex(Constants.PHONE)));
			SessionUserBean.setLat(c.getDouble(c.getColumnIndex(Constants.LAT)));
			SessionUserBean.setLng(c.getDouble(c.getColumnIndex(Constants.LNG)));
			SessionUserBean.setRange(c.getDouble(c.getColumnIndex(Constants.RANGE)));
			SessionUserBean.setService(c.getInt(c.getColumnIndex(Constants.SERVICE)));
			SessionUserBean.setPush(c.getInt(c.getColumnIndex(Constants.PUSH)));
			Log.d(tag, "session on for " + SessionUserBean.getEmail() + " id:" + SessionUserBean.getId() + ", redirecting to MainActivity");
			final Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);

		} else {
			Log.d(tag, "First Installation, there is not any user on local database");
			setContentView(R.layout.auth);
			btnAuthSignUp = (Button) findViewById(R.id.btnAuthSignUp);
			btnAuthSignUp.setOnClickListener(this);
			btnAuthFBLogin = (Button) findViewById(R.id.btnAuthFBLogin);
			btnAuthFBLogin.setOnClickListener(this);
			btnAuthLogin = (Button) findViewById(R.id.btnAuthLogin);
			btnAuthLogin.setOnClickListener(this);
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		db.close();
	}

	@Override
	public void onClick(View v) {
		final Intent intent;
		switch (v.getId()) {
		case R.id.btnAuthFBLogin:
			Log.d(tag, "btnAuthFBLogin clicked");
			intent = new Intent(this, FBLoginActivity.class);
			startActivity(intent);
			break;
		case R.id.btnAuthSignUp:
			Log.d(tag, "btnAuthSignup clicked, redirecting to SignUpActivity");
			intent = new Intent(this, SignUpActivity.class);
			startActivity(intent);
			break;
		case R.id.btnAuthLogin:
			Log.d(tag, "btnAuthLogin clicked");
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			break;
		}
	}
}
