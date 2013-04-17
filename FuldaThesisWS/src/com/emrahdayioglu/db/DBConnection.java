package com.emrahdayioglu.db;
/**
* Basic databse tasks, connect, close connection, rollback etc.
* @author Emrah Dayioglu
* @since 01.05.2012
* @version 1.0.0.1
*/

import com.emrahdayioglu.Constants;
import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
	/*
	 * Basic functions for DB interactions
	 */

	protected static Connection getConnection() throws Exception {
		Connection conn = null;
		Class.forName("com.mysql.jdbc.Driver");
		conn = (Connection) DriverManager.getConnection(Constants.DB_URL+ Constants.DB_NAME, Constants.DB_USER, Constants.DB_PASS);
		return conn;
	}

	protected static void closeConnection(Connection conn) throws SQLException {
		if (conn != null) {
			if (!conn.isClosed()) {
				conn.close();
			}
		}
	}

	protected static void rollbackConnection(Connection conn)
			throws SQLException {
		if (conn != null) {
			if (!conn.isClosed()) {
				conn.rollback();
			}
		}
	}

	protected static void commitConnection(Connection conn) throws SQLException {
		if (conn != null) {
			if (!conn.isClosed()) {
				conn.commit();
				conn.setAutoCommit(true);
			}
		}
	}

	protected static void closeStatement(Statement st) throws SQLException {
		if (st != null) {
			if (!st.isClosed()) {
				st.close();
			}
		}
	}

	protected static void closeResultSet(ResultSet rs) throws SQLException {
		if (rs != null) {
			if (!rs.isClosed()) {
				rs.close();
			}
		}

	}

	public static int getFoundRows(Connection conn) throws Exception {
		if (conn == null) {
			throw new Exception("Connection is null!");
		} else {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = (PreparedStatement) conn
						.prepareStatement("select FOUND_ROWS()");
				rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			} catch (Exception e) {
				throw new Exception("getFoundRows: " + e.getMessage());
			} finally {
				closeResultSet(rs);
				closeStatement(ps);
			}
		}
	}

	public static int getLastInsertedId(Connection conn) throws Exception {
		if (conn == null) {
			throw new Exception("Connection is null!");
		} else {
			PreparedStatement ps = null;
			ResultSet rs = null;
			try {
				ps = (PreparedStatement) conn
						.prepareStatement("select LAST_INSERT_ID()");
				rs = ps.executeQuery();
				if (rs.next()) {
					return rs.getInt(1);
				} else {
					return 0;
				}
			} catch (Exception e) {
				throw new Exception("getFoundRows: " + e.getMessage());
			} finally {
				closeResultSet(rs);
				closeStatement(ps);
			}
		}
	}
}
