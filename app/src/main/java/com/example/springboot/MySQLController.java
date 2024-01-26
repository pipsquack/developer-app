package com.example.springboot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

@RestController
public class MySQLController {

	Logger logger = LoggerFactory.getLogger("developer-app");

	@GetMapping(value = "/mysql/query", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> query(final HttpServletResponse response) {
		try {
			ServletOutputStream out = response.getOutputStream();
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://mysql.developer-app/employees?autoReconnect=true&useSSL=false", "frieren", "b3y@nd");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("select * from employees limit 100");
			while (rs.next())
				out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			con.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("SQLState: " + e.getSQLState());
			logger.error("VendorError: " + e.getErrorCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}

	@GetMapping(value = "/mysql/query_v2", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> query_v2(final HttpServletResponse response) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://mysql.developer-app/employees?autoReconnect=true&useSSL=false", "frieren", "b3y@nd");
			Statement stmt = con.createStatement();
			stmt.execute("select * from employees limit 100");
			stmt.close();
			con.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("SQLState: " + e.getSQLState());
			logger.error("VendorError: " + e.getErrorCode());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return new ResponseEntity<String>(HttpStatus.OK);
	}
}
