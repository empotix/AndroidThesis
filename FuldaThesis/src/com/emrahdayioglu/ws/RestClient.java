package com.emrahdayioglu.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

import com.emrahdayioglu.Constants;
import com.emrahdayioglu.beans.BrandBean;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.NotificationBean;
import com.emrahdayioglu.beans.SessionUserBean;
import com.emrahdayioglu.beans.UserBean;
import com.google.gson.Gson;

public class RestClient {

	// Base methods

	public static String callRestWSForJson(String params) {

		String jsonString = "";
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
		HttpConnectionParams.setSoTimeout(httpParams, 5000);
		DefaultHttpClient httpclient = new DefaultHttpClient(httpParams);
		HttpPost httpPost = new HttpPost(Constants.URL + params);
		HttpResponse httpResponse;
		try {
			httpResponse = httpclient.execute(httpPost);
			StatusLine requestSuccess = httpResponse.getStatusLine();
			if (requestSuccess.getStatusCode() == 200) {
				HttpEntity entity = httpResponse.getEntity();
				if (entity != null) {
					InputStream instream = entity.getContent();
					jsonString = convertStreamToString(instream);
					Log.d("jsonString from WS:", jsonString);
					instream.close();
				}
			} else {
				return "Error during server connection:" + requestSuccess.getStatusCode();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public static UserBean isUserValid(String email, String password) throws Exception {
		
		String jsonString = callRestWSForJson("isUserValid?email=" + email + "&password=" + password);
		UserBean ub = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			ub = gson.fromJson(jsonString, UserBean.class);
		}
		return ub;

	}

	public static boolean getUserInfo(String email, String password) {
		String jsonString = callRestWSForJson("getUserInfo?email=" + email + "&password=" + password);
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			UserBean ub = gson.fromJson(jsonString, UserBean.class);
			SessionUserBean.setId(ub.getId());
			SessionUserBean.setEmail(ub.getEmail());
			SessionUserBean.setTokenId(ub.getTokenId());
			SessionUserBean.setName(ub.getName());
			SessionUserBean.setSurname(ub.getSurname());
			SessionUserBean.setBirthday(ub.getBirthday());
			SessionUserBean.setPhoneNumber(ub.getPhoneNumber());
			return true;
		} else {
			return false;
		}
	}

	public static UserBean signUp(String name, String surname, String email, String password, String birthday, String phone) {
		String jsonString = callRestWSForJson("signUp?name=" + name + "&surname=" + surname + "&email=" + email + "&password=" + password + "&birthday=" + birthday + "&phone=" + phone);
		UserBean ub = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			ub = gson.fromJson(jsonString, UserBean.class);
		}
		return ub;
	}

	public static String loginViaFacebook(String email, long fbId, String name, String surname) {
		String jsonString = "";
		try {
			jsonString = callRestWSForJson("loginViaFacebook?email=" + email + "&fbid=" + fbId + "&name=" + name + "&surname=" + surname);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonString;
	}

	public static ArrayList<CouponBean> getCouponsByLocation(double lat, double lng, double range, int limit, boolean sortDistance, boolean sortStartDate, boolean sortEndDate, boolean sortCouponName, boolean sortBrandName, boolean sortRating, int sort, String search, int brandId) throws JSONException {
		String jsonString = callRestWSForJson("getCouponsByLocation?lat=" + lat + "&lng=" + lng + "&range=" + range + "&limit=" + limit + "&sd=" + sortDistance + "&ssd=" + sortStartDate + "&sed=" + sortEndDate + "&scn=" + sortCouponName + "&sbn=" + sortBrandName + "&sr=" + sortRating + "&sort=" + sort + "&search=" + search + "&bid=" + brandId);
		ArrayList<CouponBean> al = null;
		if (!"".equals(jsonString) || jsonString != null || jsonString != "[]") {
			JSONArray jsonArray = new JSONArray(jsonString);
			al = new ArrayList<CouponBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Gson gson = new Gson();
				CouponBean cb = gson.fromJson(jsonObject.toString(), CouponBean.class);
				al.add(cb);
			}
		}
		return al;
	}

	public static CouponBean getCoupon(int couponId, int userId, double lat, double lng) {
		String jsonString = callRestWSForJson("getCoupon?cid=" + couponId + "&uid=" + userId + "&lat=" + lat + "&lng=" + lng + "&tokenid=" + SessionUserBean.getTokenId());
		CouponBean cb = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			cb = gson.fromJson(jsonString, CouponBean.class);
		}
		return cb;
	}

	public static CouponBean getCouponCode(int couponId, int userId, double lat, double lng) {
		
		String jsonString = callRestWSForJson("getCouponCode?cid=" + couponId + "&uid=" + userId + "&lat=" + lat + "&lng=" + lng + "&tokenid=" + SessionUserBean.getTokenId());
		CouponBean cb = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			cb = gson.fromJson(jsonString, CouponBean.class);
		}
		return cb;
		
	}

	public static CouponBean addMyFavorites(int couponId, int userId) {

		String jsonString = callRestWSForJson("addMyFavorites?cid=" + couponId + "&uid=" + userId + "&tokenid=" + SessionUserBean.getTokenId());
		CouponBean cb = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			cb = gson.fromJson(jsonString, CouponBean.class);
		}
		return cb;

	}

	public static ArrayList<CouponBean> getFavCoupons(int userId, String search) throws JSONException {
		String jsonString = callRestWSForJson("getFavCoupons?uid=" + userId + "&search=" + search + "&tokenid=" + SessionUserBean.getTokenId());
		ArrayList<CouponBean> al = null;
		if (!"".equals(jsonString) || jsonString != null || jsonString != "[]") {
			JSONArray jsonArray = new JSONArray(jsonString);
			al = new ArrayList<CouponBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Gson gson = new Gson();
				CouponBean cb = gson.fromJson(jsonObject.toString(), CouponBean.class);
				al.add(cb);
			}
		}
		return al;
	}
	
	public static CouponBean deleteFavCoupons(int userId, String coupons) {

		String jsonString = callRestWSForJson("deleteFavCoupons?uid=" + userId + "&cid=" + coupons + "&tokenid=" + SessionUserBean.getTokenId());
		CouponBean cb = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			cb = gson.fromJson(jsonString, CouponBean.class);
		}
		return cb;
	}

	public static ArrayList<CouponBean> getMyCoupons(int userId, String search) throws JSONException {
		String jsonString = callRestWSForJson("getMyCoupons?uid=" + userId + "&search=" + search + "&tokenid=" + SessionUserBean.getTokenId());
		ArrayList<CouponBean> al = null;
		if (!"".equals(jsonString) || jsonString != null || jsonString != "[]") {
			JSONArray jsonArray = new JSONArray(jsonString);
			al = new ArrayList<CouponBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Gson gson = new Gson();
				CouponBean cb = gson.fromJson(jsonObject.toString(), CouponBean.class);
				al.add(cb);
			}
		}
		return al;
	}

	public static ArrayList<BrandBean> getBrands(String search) throws JSONException {
		String jsonString = callRestWSForJson("getBrands?search=" + search);
		ArrayList<BrandBean> al = null;
		if (!"".equals(jsonString) || jsonString != null || jsonString != "[]") {
			JSONArray jsonArray = new JSONArray(jsonString);
			al = new ArrayList<BrandBean>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Gson gson = new Gson();
				BrandBean bb = gson.fromJson(jsonObject.toString(), BrandBean.class);
				al.add(bb);
			}
		}
		return al;
	}

	

	public static NotificationBean sendServiceInfo(int userId, double lat, double lng) {
		String jsonString = callRestWSForJson("sendServiceInfo?uid=" + userId + "&lat=" + lat + "&lng=" + lng + "&tokenid=" + SessionUserBean.getTokenId());
		NotificationBean nb = null;
		if (!"".equals(jsonString) || jsonString != null) {
			Gson gson = new Gson();
			nb = gson.fromJson(jsonString, NotificationBean.class);
		}
		return nb;
	}
	
	public static int updateUserInfo(int userId, String name, String surname, String password, String birthdate, String phone) {
		String jsonString = callRestWSForJson("updateUserInfo?uid=" + userId + "&name=" + name + "&surname=" + surname + "&password=" + password + "&birthdate=" + birthdate + "&phone=" + phone +"&tokenid=" + SessionUserBean.getTokenId());
		return Integer.parseInt(jsonString);
	}

}
