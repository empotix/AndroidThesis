package com.emrahdayioglu.activities;
/**
* This class is extended from Android Activity
* Is responsible to login over facebook
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;
import org.json.JSONObject;

import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.db.MyDB;
import com.emrahdayioglu.ws.RestClient;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Util;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class FBLoginActivity extends Activity {

	public static final String tag = "FBLoginActivity";
	private Facebook mFacebook;
	public static final String APP_ID = "245733912141732";
	private AsyncFacebookRunner mAsyncRunner;
	private static final String[] PERMS = new String[] { "read_friendlists", "publish_stream" };
	private SharedPreferences sharedPrefs;
	private Context mContext;
	private String userData = "";
	private MyDB db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(tag, "connecting to facebook...");
		mContext = this;
		mFacebook = new Facebook(APP_ID);
		mAsyncRunner = new AsyncFacebookRunner(mFacebook);
		if (isSession()) {
			mAsyncRunner.request("me", new IDRequestListener());
			// Log.d(TAG, "getting user friends");
			// mAsyncRunner.request("me/friends", new friendsRequestListener());
		} else {
			Log.d(tag, "sessionNOTValid, relogin");
			mFacebook.authorize(this, PERMS, new LoginDialogListener());
		}
	}

	public void postToWall(String msg) {
		try {
			String response = mFacebook.request("me");
			Bundle parameters = new Bundle();
			parameters.putString("message", msg);
			parameters.putString("description", "test test test");
			response = mFacebook.request("me/feed", parameters, "POST");
			if (response == null || response.equals("") || response.equals("false")) {
				Log.v(tag, "Blank response");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isSession() {
		sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
		String access_token = sharedPrefs.getString("access_token", "x");
		Long expires = sharedPrefs.getLong("access_expires", -1);
		if (access_token != null && expires != -1) {
			mFacebook.setAccessToken(access_token);
			mFacebook.setAccessExpires(expires);
		}
		return mFacebook.isSessionValid();
	}

	private class LoginDialogListener implements DialogListener {

		@Override
		public void onComplete(Bundle values) {
			String token = mFacebook.getAccessToken();
			long token_expires = mFacebook.getAccessExpires();
			sharedPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
			sharedPrefs.edit().putLong("access_expires", token_expires).commit();
			sharedPrefs.edit().putString("access_token", token).commit();
			mAsyncRunner.request("me", new IDRequestListener());
		}
		@Override
		public void onFacebookError(FacebookError e) {
		}
		@Override
		public void onError(DialogError e) {
		}
		@Override
		public void onCancel() {
		}

	}

	private class IDRequestListener implements RequestListener {

		@Override
		public void onComplete(String response, Object state) {
			try {
				JSONObject json = Util.parseJson(response);
				userData = userData + json;
				SessionUserBean.setEmail(json.getString("email"));
				SessionUserBean.setFbid(Long.parseLong(json.getString("id")));
				SessionUserBean.setName(json.getString("first_name"));
				SessionUserBean.setSurname(json.getString("last_name"));

				String tokenId= RestClient.loginViaFacebook(SessionUserBean.getEmail(), SessionUserBean.getFbid(), SessionUserBean.getName(), SessionUserBean.getSurname());
				//TODO facebook dan expires diye bir time value donuyor, bu donen value bi yere kaydedilmeli
				// ve expire olunca local db de de expire olmali ki AuthActivity tekrar login ekranina yonlendirsin
				
				if (!"".equals(tokenId) & tokenId != null ) {
					try {
						db = new MyDB(FBLoginActivity.this);
						db.open();
						db.insertUser(SessionUserBean.getFbid(), SessionUserBean.getEmail(), tokenId, SessionUserBean.getName(), SessionUserBean.getSurname());
						db.close();
						Intent intent = new Intent(FBLoginActivity.this, MainActivity.class);
						startActivity(intent);
					} catch (Exception e) {
						Log.d(tag, "Exception when inserting local DB" + e.getMessage());
					}

				} else {
					Toast.makeText(FBLoginActivity.this, "Problem during syncronizing facebook and server info", Toast.LENGTH_LONG);
					Intent intent = new Intent(FBLoginActivity.this, MainActivity.class);
					startActivity(intent);
				}

			} catch (JSONException e) {
				Log.d(tag, "JSONException: " + e.getMessage());
			} catch (FacebookError e) {
				Log.d(tag, "FacebookError: " + e.getMessage());
			}
		}

		@Override
		public void onIOException(IOException e, Object state) {
			Log.d(tag, "IOException: " + e.getMessage());
		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e, Object state) {
		}

		@Override
		public void onMalformedURLException(MalformedURLException e, Object state) {
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mFacebook.authorizeCallback(requestCode, resultCode, data);
	}
}
