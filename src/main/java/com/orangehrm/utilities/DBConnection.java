package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.baseclass.BaseClass;

public class DBConnection {
	
	private static final String DB_URL = "jdbc:mysql://localhost:3307/orangehrm1";
	private static final String DB_USERNAME = "root";
	private static final String DB_PASSWORD = "";
	private static final Logger logger = BaseClass.logger;
	
	public static Connection getDBConnection()
	{
		try {
			logger.info("Starting DB connection");
			Connection conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			logger.info("DB connection Successful");
			return conn;
		} catch (SQLException e) {
		
			logger.info("DB connection failed");
			e.printStackTrace();
			
			return null;
		}
		
		
	}
	//get employee details from database and store in a map	
	public static Map<String, String> getEmployeeDetails(String employee_id)
	{
		String query = "Select emp_firstname, emp_middle_name, emp_lastname from hs_hr_employee where employee_id =" +employee_id;
		
		Map<String, String> employeeDetails = new HashMap<>();
		
		try(Connection con = getDBConnection();
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery(query))
		{
			logger.info("Executing Query" +query);
			if(rs.next())
			{
				String firstName = rs.getString("emp_firstname");
				String middleName = rs.getString("emp_middle_name");
				String lastname = rs.getString("emp_lastname");
				
				//Store in a MAP
				employeeDetails.put("emp_FirstName", firstName);
				employeeDetails.put("emp_MiddleName", middleName!=null?middleName:" ");
				employeeDetails.put("emp_LastName", lastname);
				
				logger.info("Query executed successfully");
				logger.info("Employee data Fetched");
				}
			else {
				logger.info("Employee not found");
			}
		}
		catch(Exception e)
		{
			logger.info("Error while executing query");
			e.printStackTrace();
		}
		return employeeDetails;
	}

}
