package com.emrahdayioglu;


/**
* This class has static settings for Application Errors
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

public class Errors {

	/**
	* Error priority settings
	*/
	public static int ERROR_PRIORITY_HIGH = 3;
	public static int ERROR_PRIORITY_MEDIUM = 2;
	public static int ERROR_PRIORITY_LOW = 1;
	
	/**
	* Error Message settings
	*/
	public static int ERROR_ID_GENERAL = 1;
	public static int ERROR_ID_ALREADY_NOTIFIED = 2;
	public static int ERROR_ID_NOT_IN_RANGE = 3;
	public static int ERROR_ID_USER_NOT_VALID = 4;
	public static int ERROR_ID_USER_ALREADY_THERE = 5;
	public static int ERROR_ID_COUPON_IS_NOT_AVAILABLE = 6;
	public static int ERROR_ID_WRONG_LOCATION = 7;
	public static int ERROR_ID_DUPLICATE_CHECKIN = 8;
	public static int ERROR_ID_DUPLICATE_FAVORITE = 9;
	
	public static String ERROR_MESSAGE_GENERAL = "An error occured";

}
