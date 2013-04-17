package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener
* It is responsible to login with username and password
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/


import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.beans.UserBean;
import com.emrahdayioglu.db.MyDB;
import com.emrahdayioglu.ws.RestClient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {

	private static final String tag = "LoginActivity";

	private EditText txtLoginEmail;
	private EditText txtLoginPassword;
	private Button btnLoginLogin;
	private LinearLayout layoutError;
	private TextView txtError;

	MyDB db;
	private String email;
	private String password;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		txtLoginEmail = (EditText) findViewById(R.id.txtLoginEmail);
		txtLoginPassword = (EditText) findViewById(R.id.txtLoginPassword);
		btnLoginLogin = (Button) findViewById(R.id.btnLoginLogin);
		btnLoginLogin.setOnClickListener(this);

		layoutError = (LinearLayout) findViewById(R.id.linearLayoutError);
		txtError = (TextView) findViewById(R.id.txtError);

		if (Util.checkConnection(this) == null) {
			txtError.setText("Check your internet connection !!!");
			layoutError.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {
		final Intent intent;
		switch (v.getId()) {
		case R.id.btnLoginLogin:
			email = txtLoginEmail.getText().toString();
			password = txtLoginPassword.getText().toString();
			if (!"".equals(email) & email != null & !"".equals(password) & password != null) {
				if (Util.regExCheck("email", email)) {
					try {
						UserBean user = RestClient.isUserValid(Util.urlEncoder(email), Util.urlEncoder(password));
						if (user.getErrorId() == 0) {
								Log.d(tag, "login user valid");
								boolean resultBasicInfo = RestClient.getUserInfo(Util.urlEncoder(email), Util.urlEncoder(password));
								if (resultBasicInfo) {
									Log.d(tag, "inserting user info to DB");
									try {
										db = new MyDB(this);
										db.open();
										db.insertUser(SessionUserBean.getId(), SessionUserBean.getEmail(), SessionUserBean.getTokenId(), SessionUserBean.getName(), SessionUserBean.getSurname(), SessionUserBean.getBirthday(), SessionUserBean.getPhoneNumber());
										SessionUserBean.setRange(10);
										Log.d(tag, "inserting to local DB is successful, redirctiong to MainActivity");
										Toast.makeText(this, "inserting to local DB is successful, redirctiong to MainActivity", Toast.LENGTH_LONG);
										intent = new Intent(this, MainActivity.class);
										startActivity(intent);
									} catch (Exception e) {
										Log.d(tag, e.getMessage());
										txtError.setText("Error when inserting local database!!!");
										layoutError.setVisibility(View.VISIBLE);
									} finally {
										db.close();
									}
								} else {
									txtError.setText("Error while receiving user info");
									layoutError.setVisibility(View.VISIBLE);
								}

						} else {
							txtError.setText("Error"+user.getErrorMessage());
							layoutError.setVisibility(View.VISIBLE);
						}
					} catch (Exception e) {
						Log.d(tag, e.getMessage());
						txtError.setText("Unexpected Error !!!");
						layoutError.setVisibility(View.VISIBLE);
					}
				} else {
					txtError.setText("Email format is not accepted!!!");
					layoutError.setVisibility(View.VISIBLE);
				}
			} else {
				txtError.setText("Email and password cannot be empty !!!");
				layoutError.setVisibility(View.VISIBLE);
			}

		}
	}

}
