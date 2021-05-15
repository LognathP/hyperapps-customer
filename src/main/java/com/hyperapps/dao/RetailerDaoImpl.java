package com.hyperapps.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.hyperapps.constants.RetailerQueryConstants;
import com.hyperapps.logger.HyperAppsLogger;


@Component
public class RetailerDaoImpl implements RetailerDao {
	
	@Autowired
	HyperAppsLogger LOGGER;
	
	@Autowired
	JdbcTemplate jdbctemp;
	
	
	
	@Override
	public boolean isStoreAvailable(int retailerId) {
		Connection connection = null;
		PreparedStatement preStmt = null;
		ResultSet res = null;
		boolean stat = false;
		try {
			connection = jdbctemp.getDataSource().getConnection();
			preStmt = connection.prepareStatement(RetailerQueryConstants.STORE_ID_CHECK);
			preStmt.setInt(1, retailerId);
			res = preStmt.executeQuery();
			if (res.next()) {
				if(res.getInt(1)>0)
				{
					stat = true;
				}
			}

		} catch (Exception e) {
			LOGGER.debug(this.getClass(), "ERROR IN DB WHILE isStoreAvailable " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				preStmt.close();
				connection.close();
				res.close();
			} catch (SQLException e) {
				e.printStackTrace();
				LOGGER.error(this.getClass(), "ERROR IN DB WHILE CLOSING DB ON isStoreAvailable " + e.getMessage());
			}

		}
		return stat;
	}
	
	
	
	
	
}
