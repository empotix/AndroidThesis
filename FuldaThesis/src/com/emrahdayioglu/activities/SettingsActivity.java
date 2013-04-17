package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity and implement OnClickListener and OnSeekBarChangeListener
* it is  responsible to show setting view
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import java.util.Calendar;

import com.emrahdayioglu.Constants;
import com.emrahdayioglu.R;
import com.emrahdayioglu.Util;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.db.MyDB;
import com.emrahdayioglu.ws.RestClient;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class SettingsActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {
	private static final String tag = "SettingsActivity";

	MyDB db;
	private double range;
	private double rangeOnView;

	private Button btnAccount;
	private Dialog dialogAccount;
	private SeekBar seekBarRange;
	private TextView txtRange;
	private Button btnSatellites;
	private CheckBox checkBoxService;
	private CheckBox checkBoxPush;
	private EditText editTextName;
	private EditText editTextSurname;
	private EditText editTextPassword;
	private EditText editTextPassword2;
	private EditText editTextBirthdate;
	private EditText editTextPhone;
	private Button btnSave;
	private LinearLayout layoutError;
	private TextView txtError;
	private Button btnSingOut;
	

	private int year;
	private int month;
	private int day;
	static final int DATE_DIALOG_ID = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		db = new MyDB(this);
		db.open();
		Cursor c = db.getUsers();
		startManagingCursor(c);
		if (c.moveToFirst()) {

			btnAccount = (Button) findViewById(R.id.btnAccount);
			btnAccount.setText(c.getString(c.getColumnIndex(Constants.EMAIL)));
			btnAccount.setOnClickListener(this);
			dialogAccount = new Dialog(this);
			dialogAccount.setContentView(R.layout.dialog_settings_account);
			dialogAccount.setTitle("Account");
			dialogAccount.getWindow().getAttributes().width = LayoutParams.FILL_PARENT;
			editTextName = (EditText) dialogAccount.findViewById(R.id.editTextName);
			editTextName.setText(SessionUserBean.getName());
			editTextSurname = (EditText) dialogAccount.findViewById(R.id.editTextSurname);
			editTextSurname.setText(SessionUserBean.getSurname());
			editTextPassword = (EditText) dialogAccount.findViewById(R.id.editTextPassword);
			editTextPassword2 = (EditText) dialogAccount.findViewById(R.id.editTextPassword2);
			editTextBirthdate = (EditText) dialogAccount.findViewById(R.id.editTextBirthdate);
			editTextBirthdate.setText(SessionUserBean.getBirthday());
			editTextBirthdate.setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						showDialog(DATE_DIALOG_ID);
					}
				}
			});

			final Calendar calendar = Calendar.getInstance();
			year = calendar.get(Calendar.YEAR);
			month = calendar.get(Calendar.MONTH);
			day = calendar.get(Calendar.DAY_OF_MONTH);

			editTextPhone = (EditText) dialogAccount.findViewById(R.id.editTextPhone);
			editTextPhone.setText(SessionUserBean.getPhoneNumber());
			btnSave = (Button) dialogAccount.findViewById(R.id.btnSave);
			btnSave.setOnClickListener(this);
			layoutError = (LinearLayout) dialogAccount.findViewById(R.id.linearLayoutError);
			txtError = (TextView) dialogAccount.findViewById(R.id.txtError);

			txtRange = (TextView) findViewById(R.id.txtRange);
			range = c.getDouble(c.getColumnIndex(Constants.RANGE));
			seekBarRange = (SeekBar) findViewById(R.id.seekBarRange);
			seekBarRange.setOnSeekBarChangeListener(this);
			seekBarRange.setProgress((int) (range * 10));

			btnSatellites = (Button) findViewById(R.id.btnSatellites);
			btnSatellites.setOnClickListener(this);
			btnSingOut = (Button) findViewById(R.id.btnSingOut);
			btnSingOut.setOnClickListener(this);

			checkBoxService = (CheckBox) findViewById(R.id.checkBoxService);
			checkBoxPush = (CheckBox) findViewById(R.id.checkBoxPush);

			if (SessionUserBean.getPush() == 1) {
				checkBoxPush.setVisibility(View.VISIBLE);
				checkBoxPush.setChecked(true);
			}

			if (SessionUserBean.getService() == 1) {
				checkBoxService.setChecked(true);
			}
			
			checkBoxService.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						// stops current service, set service as normal mode,
						// and re start service
						db.updateService(SessionUserBean.getId(), 1);
						SessionUserBean.setService(1);
						checkBoxPush.setVisibility(View.VISIBLE);
					} else {
						// stops current service, set service as bind mode, and
						// re start service
						db.updateService(SessionUserBean.getId(), 0);
						SessionUserBean.setService(0);
						checkBoxPush.setVisibility(View.INVISIBLE);
						db.updatePush(SessionUserBean.getId(), 0);
						SessionUserBean.setPush(0);

					}
				}
			});
			
			if(checkBoxService.getVisibility() == View.VISIBLE){
				checkBoxPush.setVisibility(View.VISIBLE);
			}

			checkBoxPush.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if (isChecked) {
						db.updatePush(SessionUserBean.getId(), 1);
						SessionUserBean.setPush(1);
					} else {
						db.updatePush(SessionUserBean.getId(), 0);
						SessionUserBean.setPush(0);
					}
				}
			});

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
		case R.id.btnAccount:
			Log.d(tag, "btnAccount clicked");
			dialogAccount.show();
			break;
		case R.id.btnSatellites:
			Log.d(tag, "btnSatellites clicked");
			intent = new Intent(SettingsActivity.this, SatellitesActivity.class);
			startActivity(intent);
			break;
		case R.id.btnSingOut:
			Log.d(tag, "btnSingOut clicked");
				int resulteDelete = db.deleteUser(SessionUserBean.getId());
				if (resulteDelete == 1){
					Log.d(tag, "Redirecting to AuthActivity");
					intent = new Intent(SettingsActivity.this, AuthActivity.class);
					startActivity(intent);
				}
				
			break;
		case R.id.btnSave:
			Log.d(tag, "btnSave clicked");
			if (Util.checkConnection(this) != null) {
				if (!"".equals(editTextName.getText().toString()) & editTextName.getText().toString() != null & !"".equals(editTextName.getText().toString()) & editTextName.getText().toString() != null) {
					if (editTextPassword2.getText().toString().equals(editTextPassword.getText().toString())) {
						int result = RestClient.updateUserInfo(SessionUserBean.getId(), Util.urlEncoder(editTextName.getText().toString()), Util.urlEncoder(editTextName.getText().toString()), Util.urlEncoder(editTextPassword.getText().toString()), Util.urlEncoder(editTextBirthdate.getText().toString()), Util.urlEncoder(editTextPhone.getText().toString()));
						if (result == 1) {
							SessionUserBean.setName(editTextName.getText().toString());
							SessionUserBean.setSurname(editTextSurname.getText().toString());
							SessionUserBean.setBirthday(editTextBirthdate.getText().toString());
							SessionUserBean.setPhoneNumber(editTextPhone.getText().toString());
							db.updateName(SessionUserBean.getId(), editTextName.getText().toString());
							db.updateSurname(SessionUserBean.getId(), editTextSurname.getText().toString());
							db.updateBirthday(SessionUserBean.getId(), editTextBirthdate.getText().toString());
							db.updatePhone(SessionUserBean.getId(), editTextPhone.getText().toString());
							dialogAccount.hide();
						} else {
							txtError.setText("error when update !!!");
							layoutError.setVisibility(View.VISIBLE);
						}
					} else {
						txtError.setText("passwords are not matched !!!");
						layoutError.setVisibility(View.VISIBLE);
					}
				}
			}
			break;
		}
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		rangeOnView = progress;
		range = rangeOnView / 10;
		txtRange.setText("Range:" + rangeOnView / 10 + " km");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		db.updateRange(SessionUserBean.getId(), range);
		SessionUserBean.setRange(range);
	}

	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int yearValue, int monthOfYear, int dayOfMonth) {
			year = yearValue;
			month = monthOfYear;
			day = dayOfMonth;
			editTextBirthdate.setText(day + "-" + month + "-" + year);
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
