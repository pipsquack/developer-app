package com.example.springboot;

import java.lang.reflect.InvocationTargetException;
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

	{
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException | ClassNotFoundException e) {
			logger.error(e.getMessage(), e);
		}
	}

	@GetMapping(value = "/mysql/simple_query", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> simple_query(final HttpServletResponse response) {
		try {
			ServletOutputStream out = response.getOutputStream();
			Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://mysql.developer-app:3306/employees?autoReconnect=true&useSSL=false", "frieren",
					"b3y@nd");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM employees LIMIT 1000");
			while (rs.next()) {
				out.println(rs.getInt(1) + "  " + rs.getString(2) + "  " + rs.getString(3));
			}
			stmt.close();
			con.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("SQLState: " + e.getSQLState());
			logger.error("VendorError: " + e.getErrorCode());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Success!", HttpStatus.OK);
	}

	@GetMapping(value = "/mysql/simple_join", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> simple_join() {
		try {
			Connection con = DriverManager.getConnection(
					"jdbc:mysql://mysql.developer-app:3306/employees?autoReconnect=true&useSSL=false", "frieren",
					"b3y@nd");
			Statement stmt = con.createStatement();
			stmt.execute("SELECT first_name, last_name, dept_name " +
					"FROM employees, departments, current_dept_emp " +
					"WHERE employees.emp_no = current_dept_emp.emp_no " +
					"AND current_dept_emp.dept_no = departments.dept_no " +
					"AND current_dept_emp.to_date = '9999-01-01' " +
					"LIMIT 100");
			stmt.close();
			con.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("SQLState: " + e.getSQLState());
			logger.error("VendorError: " + e.getErrorCode());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>("Success!", HttpStatus.OK);
	}

	@GetMapping(value = "/mysql/lock", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> lock() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://mysql.developer-app:3306/employees?autoReconnect=true&useSSL=false", "frieren",
					"b3y@nd");
			con.setAutoCommit(false);

			Statement stmt = con.createStatement();
			stmt.execute("UPDATE departments SET dept_name = CONCAT('New ', dept_name)");
			Thread.sleep(10000);
			con.rollback();
			stmt.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("SQLState: " + e.getSQLState());
			logger.error("VendorError: " + e.getErrorCode());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
					logger.error("SQLState: " + e.getSQLState());
					logger.error("VendorError: " + e.getErrorCode());
				}
			}
		}
		return new ResponseEntity<String>("Success!", HttpStatus.OK);
	}

	@GetMapping(value = "/mysql/retrieve_locked", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> retrieve_locked() {
		Connection con = null;
		try {
			con = DriverManager.getConnection(
					"jdbc:mysql://mysql.developer-app:3306/employees?autoReconnect=true&useSSL=false", "frieren",
					"b3y@nd");
			con.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
			Statement stmt = con.createStatement();
			stmt.execute("SELECT dept_name FROM departments FOR SHARE");
			stmt.close();
		} catch (SQLException e) {
			logger.error(e.getMessage(), e);
			logger.error("SQLState: " + e.getSQLState());
			logger.error("VendorError: " + e.getErrorCode());
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
					logger.error("SQLState: " + e.getSQLState());
					logger.error("VendorError: " + e.getErrorCode());
				}
			}
		}
		return new ResponseEntity<String>("Success!", HttpStatus.OK);
	}
}
