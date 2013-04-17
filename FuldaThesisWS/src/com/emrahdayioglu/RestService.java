package com.emrahdayioglu;
/**
* RestFul web sevice
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import java.sql.SQLException;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.emrahdayioglu.beans.BrandBean;
import com.emrahdayioglu.beans.NotificationBean;
import com.emrahdayioglu.beans.UserBean;
import com.emrahdayioglu.db.DBUtil;
import com.emrahdayioglu.beans.CouponBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

//TODO add delete put get methods

@Path("/landing")
public class RestService {

	@POST
	@Path("/isUserValid")
	@Produces("application/json")
	public String isUserValid(@QueryParam("email") String email, @QueryParam("password") String password) {
		
		UserBean user = new UserBean();
		user = Util.isUserValid(user, email, password);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(user);
		return json;
	}

	@POST
	@Path("/getUserInfo")
	@Produces("application/json")
	public String getUserInfo(@QueryParam("email") String email, @QueryParam("password") String password) {

		UserBean user = new UserBean();
		user = (UserBean) Util.getUserInfo(user, email, password, 1);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(user);
		return json;
	}

	@POST
	@Path("/signUp")
	@Produces("application/json")
	public String signUp(@QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("email") String email, @QueryParam("password") String password, @QueryParam("birthday") String birthday, @QueryParam("phone") String phone) {

		UserBean user = new UserBean();
		user = Util.createUser(user, name, surname, email, password, birthday, phone);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(user);
		return json;
	}

	@POST
	@Path("/getCouponsByLocation")
	@Produces("application/json")
	public String getCouponsByLocation(@QueryParam("lat") double lat, @QueryParam("lng") double lng, @QueryParam("range") double range, @QueryParam("limit") int limit, @QueryParam("sd") boolean sortDistance, @QueryParam("ssd") boolean sortStartDate, @QueryParam("sed") boolean sortEndDate, @QueryParam("scn") boolean sortCouponName,
			@QueryParam("sbn") boolean sortBrandName, @QueryParam("sr") boolean sortRating, @QueryParam("sort") int sort, @QueryParam("search") String search, @QueryParam("bid") int brandId) {

		CachedArrayList<CouponBean> coupons = new CachedArrayList<CouponBean>();
		coupons = (CachedArrayList<CouponBean>) Util.getCouponsByLocation(coupons, lat, lng, range, limit, sortDistance, sortStartDate, sortEndDate, sortCouponName, sortBrandName, sortRating, sort, search, brandId);
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupons);
		return json;

	}

	@POST
	@Path("/getCoupon")
	@Produces("application/json")
	public String getCoupon(@QueryParam("cid") int couponId, @QueryParam("uid") int userId, @QueryParam("lat") double lat, @QueryParam("lng") double lng, @QueryParam("tokenid") String tokenId) {

		CouponBean coupon = new CouponBean();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				coupon = (CouponBean) Util.getCoupon(coupon, couponId, userId, lat, lng);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupon);
		return json;

	}

	@POST
	@Path("/checkDistanceToCoupon")
	@Produces("application/json")
	public String checkDistanceToCoupon(@QueryParam("id") int id, @QueryParam("lat") double lat, @QueryParam("lng") double lng) {
		int isInRange = 0;
		try {
			isInRange  = Util.checkDistanceToCoupon(id, lat, lng);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(isInRange);
		return json;

	}

	@POST
	@Path("/getCouponCode")
	@Produces("application/json")
	public String getCouponCode(@QueryParam("cid") int couponId, @QueryParam("uid") int userId, @QueryParam("lat") double lat, @QueryParam("lng") double lng, @QueryParam("tokenid") String tokenId) {

		CouponBean coupon = new CouponBean();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				coupon = Util.getCouponCode(coupon, couponId, userId, lat, lng, tokenId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupon);
		return json;

	}

	@POST
	@Path("/addMyFavorites")
	@Produces("application/json")
	public String addMyFavorites(@QueryParam("cid") int couponId, @QueryParam("uid") int userId, @QueryParam("tokenid") String tokenId) {

		CouponBean coupon = new CouponBean();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				coupon = Util.addMyFavorites(coupon, couponId, userId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupon);
		return json;

	}
	
	@POST
	@Path("/deleteFavCoupons")
	@Produces("application/json")
	public String deleteFavCoupons(@QueryParam("uid") int userId, @QueryParam("cid") String coupons, @QueryParam("tokenid") String tokenId) {
		
		CouponBean coupon = new CouponBean();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				coupon = Util.deleteFavCoupons(coupon, userId, coupons);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupon);
		return json;

	}

	@POST
	@Path("/getFavCoupons")
	@Produces("application/json")
	public String getFavCoupons(@QueryParam("uid") int userId, @QueryParam("search") String search, @QueryParam("tokenid") String tokenId) {

		CachedArrayList<CouponBean> coupons = new CachedArrayList<CouponBean>();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				coupons = (CachedArrayList<CouponBean>) Util.getFavCoupons(coupons, userId, search);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupons);
		return json;

	}

	@POST
	@Path("/getMyCoupons")
	@Produces("application/json")
	public String getMyCoupons(@QueryParam("uid") int userId, @QueryParam("search") String search, @QueryParam("tokenid") String tokenId) {

		CachedArrayList<CouponBean> coupons = new CachedArrayList<CouponBean>();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				coupons = (CachedArrayList<CouponBean>) Util.getMyCoupons(coupons, userId, search);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(coupons);
		return json;

	}

	@POST
	@Path("/getBrands")
	@Produces("application/json")
	public String getBrands(@QueryParam("search") String search) {

		CachedArrayList<BrandBean> brands = new CachedArrayList<BrandBean>();
		brands = (CachedArrayList<BrandBean>) Util.getBrands(brands, search);

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(brands);
		return json;

	}

	

	@POST
	@Path("/sendServiceInfo")
	@Produces("application/json")
	public String sendServiceInfo(@QueryParam("uid") int userId, @QueryParam("lat") double lat, @QueryParam("lng") double lng, @QueryParam("tokenid") String tokenId) {

		NotificationBean notification = new NotificationBean();
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				notification = Util.sendServiceInfo(notification, userId, lat, lng);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(notification);
		return json;

	}

	@POST
	@Path("/loginViaFacebook")
	@Produces("application/json")
	public String loginViaFacebook(@QueryParam("email") String email, @QueryParam("fbid") long fbId, @QueryParam("name") String name, @QueryParam("surname") String surname) {

		String result = "";
		result = Util.loginViaFacebook(email, fbId, name, surname);

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(result);
		return json;

	}

	@POST
	@Path("/updateUserInfo")
	@Produces("application/json")
	public String loginViaFacebook(@QueryParam("uid") int userId, @QueryParam("name") String name, @QueryParam("surname") String surname, @QueryParam("password") String password, @QueryParam("birthdate") String birthdate, @QueryParam("phone") String phone, @QueryParam("tokenid") String tokenId) {

		int result = 0;
		try {
			if (DBUtil.confirmTokenId(userId, tokenId)) {
				result = DBUtil.updateUserInfo(userId, name, surname, password, birthdate, phone);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(result);
		return json;

	}

}