package com.emrahdayioglu.db;

import com.emrahdayioglu.Constants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class MyDB {

	private SQLiteDatabase db;
	private final Context context;
	private final MyDBHelper dbhelper;

	public MyDB(Context c) {
		context = c;
		dbhelper = new MyDBHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
	}

	public void close() {
		db.close();
	}

	public void open() throws SQLiteException {
		try {
			db = dbhelper.getWritableDatabase();
		} catch (SQLiteException ex) {
			Log.v("Open database exception", ex.getMessage());
			db = dbhelper.getReadableDatabase();
		}
	}

	public long insertUser(int id, String email, String tokenId, String name, String surname, String birthday, String phone) throws SQLiteException {
		ContentValues newUser = new ContentValues();
		newUser.put(Constants.KEY_ID, id);
		newUser.put(Constants.EMAIL, email);
		newUser.put(Constants.TOKEN_ID, tokenId);
		newUser.put(Constants.NAME, name);
		newUser.put(Constants.SURNAME, surname);
		newUser.put(Constants.BIRTHDAY, birthday);
		newUser.put(Constants.PHONE, phone);
		newUser.put(Constants.DATE, java.lang.System.currentTimeMillis());
		newUser.put(Constants.RANGE, 10);
		newUser.put(Constants.SERVICE, 0);
		newUser.put(Constants.PUSH, 0);
		return db.insert(Constants.TABLE_NAME, null, newUser);
	}

	public long insertUser(long fbid, String email, String tokenId, String name, String surname) throws SQLiteException {
		ContentValues newUser = new ContentValues();
		newUser.put(Constants.FB_ID, fbid);
		newUser.put(Constants.EMAIL, email);
		newUser.put(Constants.TOKEN_ID, tokenId);
		newUser.put(Constants.NAME, name);
		newUser.put(Constants.SURNAME, surname);
		newUser.put(Constants.DATE, java.lang.System.currentTimeMillis());
		newUser.put(Constants.RANGE, 10);
		newUser.put(Constants.SERVICE, 0);
		newUser.put(Constants.PUSH, 0);
		return db.insert(Constants.TABLE_NAME, null, newUser);
	}

	public boolean updateLocation(int id, double lat, double lng) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.LAT, lat);
		updateUser.put(Constants.LNG, lng);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updateRange(int id, double range) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.RANGE, range);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updateService(int id, int isAllowed) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.SERVICE, isAllowed);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updatePush(int id, int isAllowed) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.PUSH, isAllowed);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updateName(int id, String name ) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.NAME, name);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updateSurname(int id, String surname ) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.SURNAME, surname);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updatePhone(int id, String phone ) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.PHONE, phone);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean updateBirthday(int id, String birthday ) throws SQLiteException {
		ContentValues updateUser = new ContentValues();
		updateUser.put(Constants.BIRTHDAY, birthday);
		return db.update(Constants.TABLE_NAME, updateUser, Constants.KEY_ID + "=" + id, null) > 0;
	}

	public int deleteUser(int id) throws SQLiteException {
		return db.delete(Constants.TABLE_NAME, Constants.KEY_ID+"="+id, null);
	}
	

	public Cursor getUsers() {

		Cursor c = db.query(Constants.TABLE_NAME, null, null, null, null, null, null);
		return c;

	}

}
