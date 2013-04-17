package com.emrahdayioglu;

/**
* General settings for applicaiton
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
public class Constants {

	// TODO before deployment check these constants
	
	// Cache Times
	public static long CACHE_TIME_IN_MILLIS_ONE_SECOND = 1000; // 1 second
	public static long CACHE_TIME_IN_MILLIS_ONE_MINUTE = 60 * 1000; // 1 dakika
	public static long CACHE_TIME_IN_MILLIS_FIVE_MINUTE = 5 * 60 * 1000; // 5 dakika
	public static long CACHE_TIME_IN_MILLIS_FIFTEEN_MINUTE = 15 * 60 * 1000; // 15 dakika
	public static long CACHE_TIME_IN_MILLIS_ONE_HOUR = 60 * 60 * 1000; // 60 dakika
	public static long CACHE_TIME_IN_MILLIS_FOUR_HOUR = 4 * 60 * 60 * 1000; // 4 saat
	public static long CACHE_TIME_IN_MILLIS_ONE_DAY = 24 * 60 * 60 * 1000; // 24 saat
	
	// Database Local
//	public static String DB_URL = "jdbc:mysql://localhost:3306/";
//	public static String DB_NAME = "fulda_thesis_db";
//	public static String DB_USER = "root";
//	public static String DB_PASS = "root";
	

	// Database Server
	 public static String DB_URL = "jdbc:mysql://localhost:3306/";
	 public static String DB_NAME = "fulda_thesis_db";
	 public static String DB_USER = "root";
	 public static String DB_PASS = "emrah123";
	
	
	// Project
	public static String PROJECT_ROOT = "http://localhost:8080/FuldaThesisWS/";
//	public static String PROJECT_ROOT = "http://31.186.5.71:8080/FuldaThesisWS/";
	public static String WEB_SERVICE_ROOT = "/restfulWS/landing";
	public static String COUPON_CHECKIN_ROUTE = "/confirmCouponCheckin";
	public static String CATALINA_BASE = System.getProperty("catalina.base");
	public static String QR_FOLDER = "/webapps/QRFolder";
	public static String STORED_IMAGE_FOLDER = "/webapps/StoredImages";
	
	public static Double NOTIFICATION_RANGE = 0.1;
	
	
	// Translation
	public static String TRANSLATION_PACKAGE = "com.emrahdayioglu.translation.";
	public static String TRANSLATION_FILE = "translation";
	public static String TRANSLATION_LOCALE_SESSION_ATTRIBUTE = "locale";
	// Authentication
	public static String AUTHENTICATION_ERROR_PAGE = "../loginerror.jsp";
	public static String AUTHENTICATION_SESSION_USER_VARIABLE = "sessionUser";
	// Images
	public static String IMAGE_FOLDER = "img/";
	// Javascript
	public static String JAVASCTIPT_FOLDER = "js/";
	// Css
	public static String CSS_FOLDER = "css/";
	// Cookie
	public static int COOKIE_DEFAULT_MAX_AGE = 7 * 24 * 60 * 60;// 7 days
	
	
}
