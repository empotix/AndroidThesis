package com.emrahdayioglu.db;
/**
* Database queries
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/
import com.emrahdayioglu.beans.BrandBean;
import com.emrahdayioglu.beans.CouponBean;
import com.emrahdayioglu.beans.UserBean;
import com.emrahdayioglu.CachedArrayList;
import com.mysql.jdbc.Connection;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mysql.jdbc.PreparedStatement;

public class DBUtil extends DBConnection {

	public static UserBean isUserValid(String email, String password) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean ub = null;

		try {
			conn = getConnection();
			String sql = "select u.id, u.email, u.type " + "from users u " + "where email = ?" + "and password = ?" + "limit 1";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (!"".equals(email) || email != null) {
				ps.setString(++i, email);
			}
			if (!"".equals(password) || password != null) {
				ps.setString(++i, password);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				ub = new UserBean();
				ub.setId(rs.getInt(1));
				ub.setEmail(rs.getString(2));
				ub.setType(rs.getInt(3));
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return ub;
	}

	public static boolean checkUser(String email) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;

		try {
			conn = getConnection();
			String sql = "select u.id, u.email, u.type " + "from users u " + "where email = ?" + "limit 1";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (!"".equals(email) || email != null) {
				ps.setString(++i, email);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				result = true;
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static boolean confirmTokenId(int userId, String tokenId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean isTokenValid = false;

		try {

			conn = getConnection();

			String sql = "select * from users where id = ? and token_id = ? limit 1";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setString(2, tokenId);
			rs = ps.executeQuery();
			while (rs.next()) {
				isTokenValid = true;
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return isTokenValid;
	}

	public static UserBean getUserInfo(String email, String password, int limit, String tokenId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean ub = null;

		try {
			conn = getConnection();

			String sql = "update users" + " set token_id = ?" + " where email = ?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, tokenId);
			ps.setString(2, email);
			ps.executeUpdate();

			sql = "select id, name, surname, email, birthday, phone, type, lat, lng, token_id, creation_date " + "from users " + "where email = ? " + "and password = ? " + "limit ?";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (!"".equals(email) || email != null) {
				ps.setString(++i, email);
			}
			if (!"".equals(password) || password != null) {
				ps.setString(++i, password);
			}
			if (limit > 0) {
				ps.setInt(++i, limit);
			}
			rs = ps.executeQuery();
			while (rs.next()) {
				i = 0;
				ub = new UserBean();
				ub.setId(rs.getInt(++i));
				ub.setName(rs.getString(++i));
				ub.setSurname(rs.getString(++i));
				ub.setEmail(rs.getString(++i));
				ub.setBirthday(rs.getString(++i));
				ub.setPhoneNumber(rs.getString(++i));
				ub.setType(rs.getInt(++i));
				ub.setLat(rs.getDouble(++i));
				ub.setLng(rs.getDouble(++i));
				ub.setTokenId(rs.getString(++i));
				ub.setCreationDate(rs.getTimestamp(++i));
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return ub;
	}

	public static UserBean createUser(String name, String surname, String email, String password, String birthday, String phone) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		UserBean ub = null;
		try {
			conn = getConnection();
			String sql = "insert " + " into users(name, surname, email, password, birthday, phone, type, creation_date)" + " values(?,?,?,?,?,?,2,now())";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, surname);
			ps.setString(3, email);
			ps.setString(4, password);
			ps.setString(5, birthday);
			ps.setString(6, phone);
			int result = ps.executeUpdate();
			if (result == 1) {

				sql = "select id, name, surname, email, birthday, phone, type, lat, lng, token_id, creation_date " + "from users " + "where email = ? " + "limit 1";

				ps = (PreparedStatement) conn.prepareStatement(sql);
				int i = 0;
				if (!"".equals(email) || email != null) {
					ps.setString(++i, email);
				}
				rs = ps.executeQuery();
				while (rs.next()) {
					i = 0;
					ub = new UserBean();
					ub.setId(rs.getInt(++i));
					ub.setName(rs.getString(++i));
					ub.setSurname(rs.getString(++i));
					ub.setEmail(rs.getString(++i));
					ub.setBirthday(rs.getString(++i));
					ub.setPhoneNumber(rs.getString(++i));
					ub.setType(rs.getInt(++i));
					ub.setLat(rs.getDouble(++i));
					ub.setLng(rs.getDouble(++i));
					ub.setTokenId(rs.getString(++i));
					ub.setCreationDate(rs.getTimestamp(++i));
				}

			}
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}
		return ub;
	}

	public static int createUserByFacebook(String name, String surname, String email, String tokenId, long fbId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = getConnection();
			String sql = "insert " + "into users(name, surname, email, fbid, token_id, type, creation_date)" + " values(?,?,?,?,?,2,now())";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setString(1, name);
			ps.setString(2, surname);
			ps.setString(3, email);
			ps.setLong(4, fbId);
			ps.setString(5, tokenId);
			result = ps.executeUpdate();
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static CachedArrayList<CouponBean> getCouponsByLocation(double lat, double lng, double range, int limit, boolean sortDistance, boolean sortStartDate, boolean sortEndDate, boolean sortCouponName, boolean sortBrandName, boolean sortRating, int sort, String search, int brandId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean cb = null;
		CachedArrayList<CouponBean> al = null;

		try {

			conn = getConnection();

			String sql = "select c.id, c.branch_id, c.lat, c.lng, c.type, c.range, c.name, c.description, c.start_datetime, c.end_datetime, c.status, " + " b.name, b.address, b.is_parkplace, b.is_childcare, b.is_disabled, b.is_pet, " + " a.name, a.phone, a.image," + " (((acos(sin(( ? * pi()/180)) * "
					+ " sin((lat*pi()/180))+cos(( ? * pi()/180)) * " + " cos((lat*pi()/180)) * cos((( ?- lng)* " + " pi()/180))))*180/pi())*60*1.1515)*1.6094 as distance, " + " count(uc.coupon_id) rating " + " from branchs b, brands a, coupons c  " + " left outer join user_coupon uc" + " on c.id = uc.coupon_id" + " where " + "    ( "
					+ "        (69.1 * (lat - ?)) *" + "        (69.1 * (lat - ?))  " + "    ) + ( " + "        (69.1 * (lng - ?) * COS(? / 57.3)) *" + "        (69.1 * (lng - ?) * COS(? / 57.3))  " + "    ) < pow(( ? / 1.6094), 2) ";
			if (!"".equals(search) && search != null) {
				sql += "and (c.name like '%" + search + "%' or a.name like '%" + search + "%' or b.name like '%" + search + "%') ";
			}
			if (brandId > 0) {
				sql += " and a.id = ?";
			}
			sql += " and c.status = 1 and b.id = c.branch_id" + " and a.id = b.brand_id " + " and c.status = 1" + " group by c.id";
			sql += " order by ";
			if (sortDistance) {
				sql += "    ( " + "        (69.1 * (lat - ?)) *" + "        (69.1 * (lat - ?))  " + "    ) + ( " + "        (69.1 * (lng - ?) * COS(? / 57.3)) *" + "        (69.1 * (lng - ?) * COS(? / 57.3))  " + "    ) ";
			} else if (sortStartDate) {
				sql += " c.start_datetime ";
			} else if (sortEndDate) {
				sql += " c.end_datetime ";
			} else if (sortCouponName) {
				sql += " c.name ";
			} else if (sortBrandName) {
				sql += " a.name ";
			} else if (sortRating) {
				sql += " rating ";
			}

			if (sort == 1) {
				sql += "DESC ";
			} else {
				sql += "ASC ";
			}

			sql += " limit ?";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (lat <= 181 && lng <= 181 && range >= 0) {
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lng);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lng);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lng);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, range);
				if (brandId > 0) {
					ps.setInt(++i, brandId);
				}
				if (sortDistance) {
					ps.setDouble(++i, lat);
					ps.setDouble(++i, lat);
					ps.setDouble(++i, lng);
					ps.setDouble(++i, lat);
					ps.setDouble(++i, lng);
					ps.setDouble(++i, lat);
				}
			}
			if (limit > 0) {
				ps.setInt(++i, limit);
			}
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();

			al = new CachedArrayList<CouponBean>();
			while (rs.next()) {
				i = 0;
				cb = new CouponBean();
				cb.setId(rs.getInt(++i));
				cb.setBranchId(rs.getInt(++i));
				cb.setLat(rs.getDouble(++i));
				cb.setLng(rs.getDouble(++i));
				cb.setType(rs.getInt(++i));
				cb.setRange(rs.getInt(++i));
				cb.setName(rs.getString(++i));
				cb.setDescription(rs.getString(++i));
				cb.setStartDateTime(rs.getTimestamp(++i));
				cb.setEndDateTime(rs.getTimestamp(++i));
				cb.setStatus(rs.getInt(++i));
				cb.setBranchName(rs.getString(++i));
				cb.setBranchAddress(rs.getString(++i));
				cb.setParkplace(rs.getBoolean(++i));
				cb.setChildcare(rs.getBoolean(++i));
				cb.setDisabled(rs.getBoolean(++i));
				cb.setPet(rs.getBoolean(++i));
				cb.setBrandName(rs.getString(++i));
				cb.setBrandPhone(rs.getString(++i));
				cb.setBrandImage(rs.getString(++i));
				cb.setDistance(rs.getDouble(++i));
				cb.setRating(rs.getInt(++i));
				al.add(cb);

			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return al;
	}

	public static CouponBean getCoupon(int couponId, int userId, double lat, double lng) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean cb = null;

		try {
			conn = getConnection();

			String sql = "select " + "			c.id, c.branch_id, c.lat, c.lng, c.type, c.range, c.name, c.description, c.start_datetime, c.end_datetime, c.status, c.detail, " + "			b.name, b.district, b.address, b.is_parkplace, b.is_childcare, b.is_disabled, a.name, a.image, c.image, uc.datetime, uc.code,"
					+ "			(select count(coupon_id) from user_coupon uc where uc.coupon_id = ?) as usercoupon_count," + "			(select count(user_id) from user_favcoupon uf where uf.coupon_id = ? and uf.user_id = ?) as userfavcoupon_count,"
					+ "			(select if (count(user_id) > 0, 1, 0)  from user_favcoupon uf where uf.coupon_id = ? and uf.user_id = ?) as is_favorited," + " 			(((acos(sin(( ? * pi()/180)) * sin((lat*pi()/180))+cos(( ? * pi()/180)) * cos((lat*pi()/180)) * cos((( ? - lng)*pi()/180))))*180/pi())*60*1.1515)*1.6094 as distance"
					+ "			from  branchs b, brands a, coupons c" + "			left outer join user_coupon uc" + "			on uc.coupon_id = c.id" + "			where c.branch_id = b.id and b.brand_id = a.id" + "			and c.id = ?" + "			limit 1";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (couponId > 0 & userId > 0) {
				ps.setInt(++i, couponId);
				ps.setInt(++i, couponId);
				ps.setInt(++i, userId);
				ps.setInt(++i, couponId);
				ps.setInt(++i, userId);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lng);
				ps.setInt(++i, couponId);
			}
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				i = 0;
				cb = new CouponBean();
				cb.setId(rs.getInt(++i));
				cb.setBranchId(rs.getInt(++i));
				cb.setLat(rs.getDouble(++i));
				cb.setLng(rs.getDouble(++i));
				cb.setType(rs.getInt(++i));
				cb.setRange(rs.getInt(++i));
				cb.setName(rs.getString(++i));
				cb.setDescription(rs.getString(++i));
				cb.setStartDateTime(rs.getTimestamp(++i));
				cb.setEndDateTime(rs.getTimestamp(++i));
				cb.setStatus(rs.getInt(++i));
				cb.setDetail(rs.getString(++i));
				cb.setBranchName(rs.getString(++i));
				cb.setBranchDistrict(rs.getString(++i));
				cb.setBranchAddress(rs.getString(++i));
				cb.setParkplace(rs.getBoolean(++i));
				cb.setChildcare(rs.getBoolean(++i));
				cb.setDisabled(rs.getBoolean(++i));
				cb.setBrandName(rs.getString(++i));
				cb.setBrandImage(rs.getString(++i));
				cb.setImage(rs.getString(++i));
				cb.setCheckinDateTime(rs.getTimestamp(++i));
				cb.setCouponCode(rs.getString(++i));
				cb.setUserCouponCount(rs.getInt(++i));
				cb.setFavCouponCount(rs.getInt(++i));
				cb.setFavorited(rs.getBoolean(++i));
				cb.setDistance(rs.getDouble(++i));

			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return cb;
	}

	public static int checkDistanceToCoupon(int id, double lat, double lng) throws SQLException {

		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int isInRange = 0;

		try {

			conn = getConnection();

			String sql = "select" + " if( " + "	(((acos(sin(( ? * pi()/180)) *" + "  sin((lat*pi()/180))+cos(( ? * pi()/180)) *" + "  cos((lat*pi()/180)) * cos((( ? - lng)*" + "  pi()/180))))*180/pi())*60*1.1515)*1.6094" + "  < c.range, 1, 0)" + " from coupons c" + " where c.id = ?";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (lat <= 181 && lng <= 181) {
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lat);
				ps.setDouble(++i, lng);
			}
			if (id > 0) {
				ps.setInt(++i, id);
			}
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				isInRange = rs.getInt(1);
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return isInRange;

	}

	public static boolean checkUserCoupon(int couponId, int userId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;

		try {

			conn = getConnection();

			String sql = "select uc.user_id, uc.coupon_id from user_coupon uc " + " where user_id = ?" + " and coupon_id = ?";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			ps.setDouble(++i, userId);
			ps.setDouble(++i, couponId);
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();
			while (rs.next()) {
				result = true;
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static int createUserCoupon(int couponId, int userId, String couponCode) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = getConnection();
			String sql = "insert into user_coupon(user_id, coupon_id, status, datetime, code) " + " values (?,?,0,now(),?)";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, couponId);
			ps.setString(3, couponCode);
			System.out.println("PreparedStatement: " + ps.toString());
			result = ps.executeUpdate();
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static boolean checkMyFavorites(int couponId, int userId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean result = false;

		try {

			conn = getConnection();

			String sql = "select user_id from user_favcoupon where user_id = ? and coupon_id = ?";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setDouble(1, userId);
			ps.setDouble(2, couponId);
			
			rs = ps.executeQuery();
			while (rs.next()) {
				result = true;
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;

	}

	public static int addMyFavorites(int couponId, int userId) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = getConnection();
			String sql = "insert into user_favcoupon(user_id, coupon_id) " + " values (?,?)";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setInt(1, userId);
			ps.setInt(2, couponId);
			System.out.println("PreparedStatement: " + ps.toString());
			result = ps.executeUpdate();

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static CachedArrayList<CouponBean> getFavCoupons(int userId, String search) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean cb = null;
		CachedArrayList<CouponBean> al = null;

		try {

			conn = getConnection();

			String sql = "select c.id, c.branch_id, c.lat, c.lng, c.type, c.range, c.name, c.description, c.start_datetime, c.end_datetime, c.status," + " b.name, b.address, b.is_parkplace, b.is_childcare, b.is_disabled, b.is_pet, a.name, a.phone, a.image " + " from coupons c, user_favcoupon uf, branchs b, brands a" + " where c.id = uf.coupon_id"
					+ " and c.branch_id = b.id " + " and b.brand_id = a.id " + " and uf.user_id = ? ";
			if (!"".equals(search) && search != null) {
				sql += "and (c.name like '%" + search + "%' or a.name like '%" + search + "%' or b.name like '%" + search + "%') ";
			}
			sql += " limit 100";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (userId > 0) {
				ps.setDouble(++i, userId);
			}
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();

			al = new CachedArrayList<CouponBean>();
			while (rs.next()) {
				i = 0;
				cb = new CouponBean();
				cb.setId(rs.getInt(++i));
				cb.setBranchId(rs.getInt(++i));
				cb.setLat(rs.getDouble(++i));
				cb.setLng(rs.getDouble(++i));
				cb.setType(rs.getInt(++i));
				cb.setRange(rs.getInt(++i));
				cb.setName(rs.getString(++i));
				cb.setDescription(rs.getString(++i));
				cb.setStartDateTime(rs.getTimestamp(++i));
				cb.setEndDateTime(rs.getTimestamp(++i));
				cb.setStatus(rs.getInt(++i));
				cb.setBranchName(rs.getString(++i));
				cb.setBranchAddress(rs.getString(++i));
				cb.setParkplace(rs.getBoolean(++i));
				cb.setChildcare(rs.getBoolean(++i));
				cb.setDisabled(rs.getBoolean(++i));
				cb.setPet(rs.getBoolean(++i));
				cb.setBrandName(rs.getString(++i));
				cb.setBrandPhone(rs.getString(++i));
				cb.setBrandImage(rs.getString(++i));
				cb.setFavorited(true);
				al.add(cb);

			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return al;
	}

	public static CachedArrayList<CouponBean> getMyCoupons(int userId, String search) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		CouponBean cb = null;
		CachedArrayList<CouponBean> al = null;

		try {

			conn = getConnection();

			String sql = "select c.id, c.branch_id, c.lat, c.lng, c.type, c.range, c.name, c.description, c.start_datetime, c.end_datetime, c.status, " + 
					" b.name, b.address, b.brand_id, b.is_parkplace, b.is_childcare, b.is_disabled, a.name, a.image, uc.datetime " + 
					" from coupons c, user_coupon uc, branchs b, brands a "	+ 
					" where c.id = uc.coupon_id " + " and c.branch_id = b.id " + " and b.brand_id = a.id " + " and uc.user_id = ?";
			if (!"".equals(search) && search != null) {
				sql += "and (c.name like '%" + search + "%' or a.name like '%" + search + "%' or b.name like '%" + search + "%') ";
			}
			sql += " order by uc.datetime desc  limit 100";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (userId > 0) {
				ps.setDouble(++i, userId);
			}
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();

			al = new CachedArrayList<CouponBean>();
			while (rs.next()) {
				i = 0;
				cb = new CouponBean();
				cb.setId(rs.getInt(++i));
				cb.setBranchId(rs.getInt(++i));
				cb.setLat(rs.getDouble(++i));
				cb.setLng(rs.getDouble(++i));
				cb.setType(rs.getInt(++i));
				cb.setRange(rs.getInt(++i));
				cb.setName(rs.getString(++i));
				cb.setDescription(rs.getString(++i));
				cb.setStartDateTime(rs.getTimestamp(++i));
				cb.setEndDateTime(rs.getTimestamp(++i));
				cb.setStatus(rs.getInt(++i));
				cb.setBranchName(rs.getString(++i));
				cb.setBranchAddress(rs.getString(++i));
				cb.setParkplace(rs.getBoolean(++i));
				cb.setChildcare(rs.getBoolean(++i));
				cb.setDisabled(rs.getBoolean(++i));
				cb.setBrandName(rs.getString(++i));
				cb.setBrandPhone(rs.getString(++i));
				cb.setBrandImage(rs.getString(++i));
				cb.setCheckinDateTime(rs.getTimestamp(++i));
				al.add(cb);

			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return al;
	}

	public static CachedArrayList<BrandBean> getBrands(String search, int limit) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		BrandBean bb = null;
		CachedArrayList<BrandBean> al = null;

		try {

			conn = getConnection();

			String sql = "select a.id, a.name, a.phone, a.image, a.description" + " from brands a " + " where 1=1 ";
			if (!"".equals(search) && search != null) {
				sql += " and a.name like '%" + search + "%' ";
			}
			sql += " limit ?";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			if (limit > 0) {
				ps.setInt(++i, limit);
			}
			System.out.println("PreparedStatement: " + ps.toString());
			rs = ps.executeQuery();

			al = new CachedArrayList<BrandBean>();
			while (rs.next()) {
				i = 0;
				bb = new BrandBean();
				bb.setId(rs.getInt(++i));
				bb.setName(rs.getString(++i));
				bb.setPhone(rs.getString(++i));
				bb.setImage(rs.getString(++i));
				bb.setDescription(rs.getString(++i));
				al.add(bb);
			}

		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return al;
	}

	public static int createCoupon(int branchId, double lat, double lng, int type, String name, String description, Date startDateTime, Date endDateTime, int status, String image) throws SQLException {

		Connection conn = null;
		PreparedStatement ps = null;
		int result = 0;
		try {
			conn = getConnection();
			String sql = "insert" + " into coupons( branch_id, lat, lng, type, name, description, start_datetime, end_datetime, status, image)" + " values (?,?,?,?,?,?,now(),now(),?,?)";

			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			ps.setInt(++i, branchId);
			ps.setDouble(++i, lat);
			ps.setDouble(++i, lng);
			ps.setInt(++i, type);
			ps.setString(++i, name);
			ps.setString(++i, description);
			// TODO daha sonradan bu tarihler de aktive edilecek simdilik now
			// ps.setDate(++i, startDateTime);
			// ps.setDate(++i, endDateTime);
			ps.setInt(++i, status);
			ps.setString(++i, image);
			result = ps.executeUpdate();
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;

	}

	public static int deleteFavCoupons(int userId, int[] coupons) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = getConnection();
			for (int i = 0; i < coupons.length; i++) {
				String sql = "delete from user_favcoupon" + " where user_id = ? and coupon_id = ?";
				ps = (PreparedStatement) conn.prepareStatement(sql);
				ps.setInt(1, userId);
				ps.setInt(2, coupons[i]);
				System.out.println("PreparedStatement: " + ps.toString());
				result = ps.executeUpdate();
			}
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static int updateUserLocation(int userId, double lat, double lng) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = getConnection();

			String sql = "update users" + " set lat = ?, lng = ?" + " where id = ?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			ps.setDouble(1, lat);
			ps.setDouble(2, lng);
			ps.setInt(3, userId);
			result = ps.executeUpdate();
			System.out.println("PreparedStatement: " + ps.toString());
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

	public static int updateUserInfo(int userId, String name, String surname, String password, String birthdate, String phone) throws SQLException {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = 0;
		try {
			conn = getConnection();

			String sql = "update users" + " set name = ? ,surname = ? ";
			if (!"".equals(password) & password != null) {
				sql += ",password = ? ";
			}
			sql += ",birthday = ? ,phone = ?" + " where id = ?";
			ps = (PreparedStatement) conn.prepareStatement(sql);
			int i = 0;
			ps.setString(++i, name);
			ps.setString(++i, surname);
			if (!"".equals(password) & password != null) {
				ps.setString(++i, password);
			}
			ps.setString(++i, birthdate);
			ps.setString(++i, phone);
			ps.setInt(++i, userId);
			result = ps.executeUpdate();
			System.out.println("PreparedStatement: " + ps.toString());
		} catch (Exception ex) {
			Logger.getLogger(DBUtil.class.getName()).log(Level.SEVERE, null, ex);
		} finally {
			closeResultSet(rs);
			closeStatement(ps);
			closeConnection(conn);
		}
		return result;
	}

}

// 02.05.2012 Performansa göre eski haline alinaca kupon sorgusu
// kupon satis rating bilgisi yok
//
// String sql =
// "select c.id, c.branch_id, c.lat, c.lng, c.type, c.range, c.name, c.description, c.start_datetime, c.end_datetime, c.status, "
// +
// " b.name, b.address, b.is_parkplace, b.is_childcare, b.is_disabled, " +
// " a.name, a.phone," + " (((acos(sin(( ? * pi()/180)) * " +
// " sin((lat*pi()/180))+cos(( ? * pi()/180)) * " +
// " cos((lat*pi()/180)) * cos((( ?- lng)* " +
// " pi()/180))))*180/pi())*60*1.1515)*1.6094 as distance " +
// " from coupons c, branchs b, brands a  " +
// " where " +
// "    ( " +
// "        (69.1 * (lat - ?)) *" +
// "        (69.1 * (lat - ?))  " +
// "    ) + ( " +
// "        (69.1 * (lng - ?) * COS(? / 57.3)) *" +
// "        (69.1 * (lng - ?) * COS(? / 57.3))  " +
// "    ) < pow(( ? / 1.6094), 2) ";
// if (!"".equals(search) && search != null){
// sql+="and (c.name like '%"+search+"%' or a.name like '%"+search+"%') ";
// }
// sql+=" and b.id = c.branch_id" +
// " and a.id = b.brand_id" +
// " order by ";
// if (sortDistance){
// sql+="    ( " +
// "        (69.1 * (lat - ?)) *" +
// "        (69.1 * (lat - ?))  " +
// "    ) + ( " +
// "        (69.1 * (lng - ?) * COS(? / 57.3)) *" +
// "        (69.1 * (lng - ?) * COS(? / 57.3))  " +
// "    ) ";
// } else if (sortStartDate){
// sql+=" c.start_datetime ";
// } else if (sortEndDate){
// sql+=" c.end_datetime ";
// } else if (sortCouponName){
// sql+=" c.name ";
// } else if (sortBrandName){
// sql+=" a.name ";
// }
//
// if (sort == 1){
// sql += "DESC ";
// } else {
// sql += "ASC ";
// }
//
// sql+=" limit ?";

