package com.orangehrm.test;

import static org.testng.Assert.assertTrue;

import java.util.Map;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LogInPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtendManager;

import junit.framework.Assert;

public class DBVerificationTest extends BaseClass{
	
	private LogInPage loginpage;
	private HomePage homepage;
	
	
	@BeforeMethod
	public void setupPages()
	{
		loginpage = new LogInPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	
	@Test(dataProvider="DBVerificationData", dataProviderClass = DataProviders.class)
	public void verifyEmployeeNameVerificationFromDB(String employeeID, String employeeName)
	{
		SoftAssert softAssert = getsoftAssert();
		ExtendManager.startTest("Verify Employee name from DB"); 
		ExtendManager.logStep("Logging with admin credentials");
		loginpage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		ExtendManager.logStep("click on PIM tab");
		homepage.clickOnPIMTab();

		ExtendManager.logStep("Search for Employee");
		homepage.employeeSearch(employeeName);
		
		ExtendManager.logStep("Get the employee name from  DB");
		String employee_id = employeeID;
		
		//Fetch the data into Map
		Map<String, String> employeeDetails = DBConnection.getEmployeeDetails(employee_id);
		
		String empFirstName = employeeDetails.get("emp_FirstName");
		String empMiddleName = employeeDetails.get("emp_MiddleName");
		String empLastName = employeeDetails.get("emp_LastName");
		
	
		
		String employeeFirstandMiddleName = (empFirstName+" "+empMiddleName).trim();

		//Verify employee firstname and middlename
		ExtendManager.logStep("Verify employee firstName and MiddleName");
		softAssert.assertTrue(homepage.verifyEmployeeFirstAndMiddleName(employeeFirstandMiddleName), "First name and middle name are not matching");
		
		//Verify employee lastname
		ExtendManager.logStep("Verify employee LastName");
		softAssert.assertTrue(homepage.verifyEmployeeLastName(empLastName));
		
		ExtendManager.logStep("DB validation completed");
		
		softAssert.assertAll();
		
	}
}
