package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener
* It is responsible to signup the user
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import java.util.Calendar;
import com.emrahdayioglu.Errors;
import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.UserBean;
import com.emrahdayioglu.ws.RestClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SignUpActivity extends Activity implements OnClickListener {

	private static final String tag = "SignUpActivity";

	TextView txtSignupFirstName;
	TextView txtSignupLastName;
	TextView txtSignupEmail;
	TextView txtSignupPassword;
	TextView txtSignupPassword2;
	TextView txtSignupBirthday;
	TextView txtSignupPhone;
	Button btnSignupSignup;
	private LinearLayout layoutError;
	private TextView txtError;

	private int year;
	private int month;
	private int day;

	static final int DATE_DIALOG_ID = 0;

	private static AlertDialog alertConnectionError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.signup);

		layoutError = (LinearLayout) findViewById(R.id.linearLayoutError);
		txtError = (TextView) findViewById(R.id.txtError);

		txtSignupFirstName = (TextView) findViewById(R.id.txtSignupFirstName);
		txtSignupLastName = (TextView) findViewById(R.id.txtSignupLastName);
		txtSignupEmail = (TextView) findViewById(R.id.txtSignupEmail);
		txtSignupPassword = (TextView) findViewById(R.id.txtSignupPassword);
		txtSignupPassword2 = (TextView) findViewById(R.id.txtSignupPassword2);
		txtSignupBirthday = (TextView) findViewById(R.id.txtSignupBirthday);
		txtSignupBirthday.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					showDialog(DATE_DIALOG_ID);
				}
			}
		});
		txtSignupPhone = (TextView) findViewById(R.id.txtSignupPhone);
		btnSignupSignup = (Button) findViewById(R.id.btnSignupSignup);
		btnSignupSignup.setOnClickListener(this);

		// get the current date
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		alertConnectionError = new AlertDialog.Builder(this).create();
		alertConnectionError.setTitle("Error");
		alertConnectionError.setMessage("There is not Internet Connection!");
		alertConnectionError.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
	}

	@Override
	public void onClick(View v) {

		String name = txtSignupFirstName.getText().toString();
		String surname = txtSignupLastName.getText().toString();
		String email = txtSignupEmail.getText().toString();
		String password = txtSignupPassword.getText().toString();
		String password2 = txtSignupPassword2.getText().toString();
		String birthday = txtSignupBirthday.getText().toString();
		String phone = txtSignupPhone.getText().toString();
		if (Util.checkConnection(this) != null) {
			if (!"".equals(name) & name != null & !"".equals(surname) & surname != null & !"".equals(email) & email != null & !"".equals(password) & password != null) {
				if (Util.regExCheck("email", email)) {
					if (password.equals(password2)) {
							UserBean signupUser = RestClient.signUp(Util.urlEncoder(name), Util.urlEncoder(surname), Util.urlEncoder(email), Util.urlEncoder(password), Util.urlEncoder(birthday), Util.urlEncoder(phone));
							if (signupUser.getErrorId() == 0) {
								Log.d(tag, "SignUp successfull");
								new AlertDialog.Builder(this).setTitle("User Created").setMessage("User has been created. Please Login").setNeutralButton("Close", new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dlg, int sumthin) {
										Log.d(tag, "Redirecting to LoginActivity");
										final Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
										startActivity(intent);
									}
								}).show();

							} else if (signupUser.getErrorId() == Errors.ERROR_ID_USER_ALREADY_THERE) {
								Log.d(tag, signupUser.getErrorMessage());
								txtError.setText("email already registered");
								layoutError.setVisibility(View.VISIBLE);
							}

					} else {
						txtError.setText("Passwords are not matched !!!");
						layoutError.setVisibility(View.VISIBLE);
						txtSignupPassword.setText("");
						txtSignupPassword2.setText("");
					}
				} else {
					txtError.setText("Email format is not accepted!!!");
					layoutError.setVisibility(View.VISIBLE);
				}
			} else {
				txtError.setText("Please fill all required fields !!!");
				layoutError.setVisibility(View.VISIBLE);
			}
		} else {
			alertConnectionError.show();
		}

	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int yearValue, int monthOfYear, int dayOfMonth) {
			year = yearValue;
			month = monthOfYear;
			day = dayOfMonth;
			txtSignupBirthday.setText(day + "-" + month + "-" + year);
		}
	};

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, year, month, day);
		}
		return null;
	}
}
