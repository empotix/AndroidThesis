package com.emrahdayioglu;
/**
* This class handle basic java tasks such as time conversion, number format conversion, regular expresses
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {


	/**
	* For formatting double values
	* @param double 
	* @return int digit of double value
	*/
	public static String formatDoubleValue(double value, int digit) {
		DecimalFormat df = null;
		if (digit == 0)
			df = new DecimalFormat("#");
		else if (digit == 1)
			df = new DecimalFormat("#.#");
		else if (digit == 2)
			df = new DecimalFormat("#.##");
		else if (digit == 3)
			df = new DecimalFormat("#.###");
		else if (digit == 4)
			df = new DecimalFormat("#.####");
		else
			df = new DecimalFormat("#.#####");
		return df.format(value);
	}

	/**
	* For formatting data value to dd MMM yyyy HH:mm format
	* @param Date
	* @return string 
	*/
	public static String formatDate(Date date) {
		String dateFormatted = "";
		Format formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
		dateFormatted = formatter.format(date);
		return dateFormatted;
	}

	/**
	* For copy the stream on Async image loader
	* @param InputStream
	* @param OutputStream
	*/
	public static void copyStream(InputStream inputStream, OutputStream outputStream) {
		final int bufferSize = 1024;
		try {
			byte[] bytes = new byte[bufferSize];
			for (;;) {
				int count = inputStream.read(bytes, 0, bufferSize);
				if (count == -1)
					break;
				outputStream.write(bytes, 0, count);
			}
		} catch (Exception ex) {
		}
	}

	/**
	* Check whether there is internet connection on the device
	* @param Context
	* @return NetworkInfo
	*/
	public static NetworkInfo checkConnection(Context context) {
		ConnectivityManager connMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connMan.getActiveNetworkInfo();
		return info;
	}
	
	/**
	* Regular express to check email format
	* @param string check content
	* @param string
	* @return boolean
	*/
	public static boolean regExCheck(String check, String string){
		String regEx = ""; 
		if ("email".equals(check)){
			regEx = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		} 
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(string);
		boolean match = m.matches();
		return match;
	}

	/**
	* encode the url to utf-8
	* @param String url
	* @return String encoded url
	*/
	public static String urlEncoder(String rowUrl){
		
		String encodedurl="";
		try {
			encodedurl = URLEncoder.encode(rowUrl,"UTF-8"); 
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encodedurl;
	}

}
