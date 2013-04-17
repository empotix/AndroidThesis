package com.emrahdayioglu.db;

import com.emrahdayioglu.Constants;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDBHelper extends SQLiteOpenHelper{

	private static final String CREATE_TABLE = "create table "+Constants.TABLE_NAME
			+ " ( "
			+ Constants.KEY_ID + " integer primary key, "
			+ Constants.TOKEN_ID + " text, "
			+ Constants.FB_ID + " long, "
			+ Constants.EMAIL + " text not null, "
			+ Constants.NAME + " name, "
			+ Constants.SURNAME + " surname, "
			+ Constants.BIRTHDAY + " birthday, "
			+ Constants.PHONE + " phone, "
			+ Constants.LAT + " double, "
			+ Constants.LNG + " double, "
			+ Constants.DATE + " long,"
			+ Constants.RANGE + " double,"
			+ Constants.SERVICE + " integer,"
			+ Constants.PUSH + " integer"
			+");";
	
	public MyDBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.v("MyDBHelper onCreate", "creating tables");
		try{
			db.execSQL(CREATE_TABLE);
		} catch (SQLiteException ex) {
			Log.v("Create table exception", ex.getMessage());
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("TaskDBAdapter", "upgrading from version "+oldVersion+" to "+newVersion+", which will destroy all old data");
		db.execSQL("drop table if exist "+Constants.TABLE_NAME);
		onCreate(db);
	}

}
