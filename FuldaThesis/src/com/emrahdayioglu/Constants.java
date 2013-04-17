package com.emrahdayioglu;

/**
* This class has static settings for Application
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

public class Constants {
	
	/**
	* Application database settings
	*/
	public static final String DATABASE_NAME = "fuldaThesisDB";
	public static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "authUsers";
	public static final String KEY_ID = "_key";
	public static final String TOKEN_ID = "tokenid";
	public static final String FB_ID = "fbid";
	public static final String EMAIL = "email";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String BIRTHDAY = "birthday";
	public static final String PHONE = "phone";
	public static final String LAT = "lat";
	public static final String LNG = "lng";
	public static final String DATE = "date";
	public static final String RANGE = "range";
	public static final String SERVICE = "service";
	public static final String PUSH = "push";

	/**
	* Local access to web service
	*/
//	public static String URL = "http://10.0.2.2:8080/FuldaThesisWS/restfulWS/landing/";
//	public static String QR_FOLDER = "http://10.0.2.2:8080/QRFolder/";
//	public static String STORED_IMAGE_FOLDER = "http://10.0.2.2:8080/StoredImages/";
	
	/**
	* Remote access to web service
	*/
	public static String URL = "http://31.186.5.71:8080/FuldaThesisWS/restfulWS/landing/";
	public static String QR_FOLDER = "http://31.186.5.71:8080/QRFolder/";
	public static String STORED_IMAGE_FOLDER = "http://31.186.5.71:8080/StoredImages/";

	
}
