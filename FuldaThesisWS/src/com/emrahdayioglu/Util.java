package com.emrahdayioglu;
/**
* handles caching, if it is required calls DBUtil to get data from database
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Hashtable;

import com.emrahdayioglu.beans.BrandBean;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.NotificationBean;
import com.emrahdayioglu.beans.UserBean;
import com.emrahdayioglu.db.DBUtil;

public class Util extends Constants {

	private static Hashtable<String, UserBean> cacheUser = new Hashtable<String, UserBean>();
	private static Hashtable<String, CachedArrayList<CouponBean>> cacheCoupons = new Hashtable<String, CachedArrayList<CouponBean>>();
	private static Hashtable<String, CouponBean> cacheCoupon = new Hashtable<String, CouponBean>();
	private static Hashtable<String, CachedArrayList<BrandBean>> cacheBrands = new Hashtable<String, CachedArrayList<BrandBean>>();
	private static Hashtable<String, NotificationBean> cacheNotification = new Hashtable<String, NotificationBean>();
	private static Hashtable<String, CachedArrayList<CouponBean>> cacheFavCoupons = new Hashtable<String, CachedArrayList<CouponBean>>();
	private static Hashtable<String, CachedArrayList<CouponBean>> cacheMyCoupons = new Hashtable<String, CachedArrayList<CouponBean>>();

	public static UserBean isUserValid(UserBean user, String email, String password) {

		try {
			user = DBUtil.isUserValid(email, password);
			if (user == null) {
				user = new UserBean();
				user.setErrorId(Errors.ERROR_ID_USER_NOT_VALID);
				user.setErrorMessage("User is not valid");
				user.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
			}
		} catch (Exception e) {
			user = new UserBean();
			user.setErrorId(Errors.ERROR_ID_GENERAL);
			user.setErrorMessage("SQLException");
			user.setErrorPriority(Errors.ERROR_PRIORITY_HIGH);
		}
		return user;
	}

	public static UserBean getUserInfo(UserBean user, String email, String password, int limit) {
		try {
			String tokenId = AppUtil.generateRandomAlpaNumeric(false);
			String key = "user#" + email + "#" + password;
			System.out.println(key);
			user = (UserBean) cacheUser.get(key);
			if (user == null || user.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_SECOND)) {
				user = DBUtil.getUserInfo(email, password, limit, tokenId);
				user.setRefreshTime(System.currentTimeMillis());
				cacheUser.put(key, user);
			}
		} catch (Exception e) {
			user = new UserBean();
			user.setErrorId(Errors.ERROR_ID_USER_NOT_VALID);
			user.setErrorMessage("User is not valid");
			user.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
		}
		return user;
	}

	public static UserBean createUser(UserBean user, String name, String surname, String email, String password, String birthday, String phone) {
		try {
			boolean result = DBUtil.checkUser(email);
			if (!result) {
				user = DBUtil.createUser(name, surname, email, password, birthday, phone);
			} else {
				user = new UserBean();
				user.setErrorId(Errors.ERROR_ID_USER_ALREADY_THERE);
				user.setErrorMessage("The user wanted to create is already there");
				user.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
			}
		} catch (Exception e) {
			user = new UserBean();
			user.setErrorId(Errors.ERROR_ID_GENERAL);
			user.setErrorMessage("SQLException");
			user.setErrorPriority(Errors.ERROR_PRIORITY_HIGH);
		}
		return user;
	}

	public static CachedArrayList<CouponBean> getCouponsByLocation(CachedArrayList<CouponBean> coupons, double lat, double lng, double range, int limit, boolean sortDistance, boolean sortStartDate, boolean sortEndDate, boolean sortCouponName, boolean sortBrandName, boolean sortRating, int sort, String search, int brandId) {
		DecimalFormat df = new DecimalFormat("#.###");
		try {
			String key = "coupons#" + df.format(lat) + "#" + df.format(lng) + "#" + range + "#" + limit + "#" + sortDistance + "#" + sortStartDate + "#" + sortEndDate + "#" + sortCouponName + "#" + sortBrandName + "#" + sortRating + "#" + sort + "#" + search + "#" + brandId;
			System.out.println(key);
			coupons = (CachedArrayList<CouponBean>) cacheCoupons.get(key);
			if (coupons == null || coupons.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_SECOND)) {
				coupons = DBUtil.getCouponsByLocation(lat, lng, range, limit, sortDistance, sortStartDate, sortEndDate, sortCouponName, sortBrandName, sortRating, sort, search, brandId);
				coupons.setRefreshTime(System.currentTimeMillis());
				cacheCoupons.put(key, coupons);
			}
		} catch (Exception e) {
		}
		return coupons;
	}

	public static CouponBean getCoupon(CouponBean coupon, int couponId, int userId, double lat, double lng) {
		try {
			String key = "coupon#" + couponId + "#" + userId;
			System.out.println(key);
			coupon = (CouponBean) cacheCoupon.get(key);
			if (coupon == null || coupon.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_SECOND)) {
				coupon = DBUtil.getCoupon(couponId, userId, lat, lng);
				coupon.setRefreshTime(System.currentTimeMillis());
				cacheCoupon.put(key, coupon);
			}
		} catch (Exception e) {
			coupon = new CouponBean();
			coupon.setErrorId(Errors.ERROR_ID_COUPON_IS_NOT_AVAILABLE);
			coupon.setErrorMessage("Coupon is not available");
			coupon.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
		}
		return coupon;
	}

	public static CouponBean getCouponCode(CouponBean coupon, int couponId, int userId, double lat, double lng, String tokenId) {

		try {

			int isInRange = DBUtil.checkDistanceToCoupon(couponId, lat, lng);
			if (isInRange == 1) {
				String couponCode = AppUtil.generateRandomAlpaNumeric(true);
				boolean result = DBUtil.checkUserCoupon(couponId, userId);
				if (!result) {
					DBUtil.createUserCoupon(couponId, userId, couponCode);
					coupon = new CouponBean();
					coupon.setId(couponId);
					coupon.setCouponCode(couponCode);
				} else {
					coupon = new CouponBean();
					coupon.setErrorId(Errors.ERROR_ID_DUPLICATE_CHECKIN);
					coupon.setErrorMessage("You hava already checked in for this coupon");
					coupon.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
				}

			} else {
				coupon = new CouponBean();
				coupon.setErrorId(Errors.ERROR_ID_WRONG_LOCATION);
				coupon.setErrorMessage("Check in is not available in this location");
				coupon.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
			}
		} catch (Exception ex) {
		}
		return coupon;
	}

	public static CouponBean addMyFavorites(CouponBean coupon, int couponId, int userId) {
		try {

			boolean isAlreadyThere = DBUtil.checkMyFavorites(couponId, userId);
			if (!isAlreadyThere) {
				DBUtil.addMyFavorites(couponId, userId);
				coupon = new CouponBean();
				coupon.setId(couponId);
			} else {
				coupon = new CouponBean();
				coupon.setErrorId(Errors.ERROR_ID_DUPLICATE_FAVORITE);
				coupon.setErrorMessage("you have already added to your favorite list");
				coupon.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
			}
		} catch (Exception e) {
			coupon = new CouponBean();
			coupon.setErrorId(Errors.ERROR_ID_GENERAL);
			coupon.setErrorMessage("Error when adding favorite coupon");
			coupon.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
		}

		return coupon;
	}

	public static CouponBean deleteFavCoupons(CouponBean coupon, int userId, String coupons) {

		int[] couponsInteger = AppUtil.parseStringToIntegerArray(coupons);
		try {
			DBUtil.deleteFavCoupons(userId, couponsInteger);
		} catch (Exception e) {
			coupon = new CouponBean();
			coupon.setErrorId(Errors.ERROR_ID_GENERAL);
			coupon.setErrorMessage("Error when deleting favorite coupon");
			coupon.setErrorPriority(Errors.ERROR_PRIORITY_MEDIUM);
		}

		return coupon;
	}

	public static CachedArrayList<CouponBean> getFavCoupons(CachedArrayList<CouponBean> coupons, int userId, String search) {
		try {
			String key = "favCoupons#" + userId + "#" + search;
			System.out.println(key);
			coupons = (CachedArrayList<CouponBean>) cacheFavCoupons.get(key);
			if (coupons == null || coupons.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_SECOND)) {
				coupons = DBUtil.getFavCoupons(userId, search);
				coupons.setRefreshTime(System.currentTimeMillis());
				cacheCoupons.put(key, coupons);
			}
		} catch (Exception e) {
		}
		return coupons;
	}

	public static CachedArrayList<CouponBean> getMyCoupons(CachedArrayList<CouponBean> coupons, int userId, String search) {
		try {
			String key = "myCoupons#" + userId + "#" + search;
			System.out.println(key);
			coupons = (CachedArrayList<CouponBean>) cacheMyCoupons.get(key);
			if (coupons == null || coupons.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_SECOND)) {
				coupons = DBUtil.getMyCoupons(userId, search);
				coupons.setRefreshTime(System.currentTimeMillis());
				cacheCoupons.put(key, coupons);
			}
		} catch (Exception e) {
		}
		return coupons;
	}

	public static ArrayList<BrandBean> getBrands(CachedArrayList<BrandBean> brands, String search) {
		try {
			String key = "brands#" + search;
			System.out.println(key);
			brands = (CachedArrayList<BrandBean>) cacheBrands.get(key);
			if (brands == null || brands.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_SECOND)) {
				brands = DBUtil.getBrands(search, 100);
				brands.setRefreshTime(System.currentTimeMillis());
				cacheBrands.put(key, brands);
			}
		} catch (Exception ex) {
		}
		return brands;
	}

	public static int createCoupon(int branchId, double lat, double lng, int type, String name, String description, Date startDateTime, Date endDateTime, int status, String image) {
		int result = 0;
		try {
			result = DBUtil.createCoupon(branchId, lat, lng, type, name, description, startDateTime, endDateTime, status, image);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String loginViaFacebook(String email, long fbId, String name, String surname) {
		String tokenId = "";
		int result = 0;
		try {
			tokenId = AppUtil.generateRandomAlpaNumeric(false);
			result = DBUtil.createUserByFacebook(name, surname, email, tokenId, fbId);
			if (result == 0) {
				tokenId = "";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tokenId;
	}

	public static NotificationBean sendServiceInfo(NotificationBean notification, int userId, double lat, double lng) {
		int isUpdated;
		try {
			isUpdated = DBUtil.updateUserLocation(userId, lat, lng);
			if (isUpdated == 1) {
				CachedArrayList<CouponBean> coupons = DBUtil.getCouponsByLocation(lat, lng, Constants.NOTIFICATION_RANGE, 1, true, false, false, false, false, false, 0, "", 0);
				if (!coupons.isEmpty()) {
					String key = "cacheNotification#" + userId + "#" + coupons.get(0).getId();
					notification = (NotificationBean) cacheNotification.get(key);
					if (notification == null || notification.needsRefresh(CACHE_TIME_IN_MILLIS_ONE_DAY)) {
						notification = new NotificationBean();
						notification.setUserId(userId);
						notification.setCouponId(coupons.get(0).getId());
						notification.setRefreshTime(System.currentTimeMillis());
						cacheNotification.put(key, notification);
					} else {
						notification.setErrorId(Errors.ERROR_ID_ALREADY_NOTIFIED);
						notification.setErrorMessage("Already Notified in this day");
						notification.setErrorPriority(Errors.ERROR_PRIORITY_LOW);
					}
				} else {
					notification.setErrorId(Errors.ERROR_ID_NOT_IN_RANGE);
					notification.setErrorMessage("There is not any coupon in range");
					notification.setErrorPriority(Errors.ERROR_PRIORITY_LOW);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return notification;
	}

	public static int checkDistanceToCoupon(int id, double lat, double lng) {
		int distance = 0;
		try {
			distance = DBUtil.checkDistanceToCoupon(id, lat, lng);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return distance;
	}

}
