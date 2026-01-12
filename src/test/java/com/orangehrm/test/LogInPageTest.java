package com.orangehrm.test;



import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LogInPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtendManager;
import com.orangehrm.utilities.RetryAnalyzer;

public class LogInPageTest extends BaseClass {
	
	private LogInPage loginpage;
	private HomePage homepage;
	SoftAssert softAssert = getsoftAssert();
	
	@BeforeMethod
	public void setupPages()
	{
		loginpage = new LogInPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	@Test(dataProvider="ValidLoginData", dataProviderClass = DataProviders.class, retryAnalyzer = RetryAnalyzer.class)
	public void verifyValidLoginTest(String Username, String Password)
	{
		ExtendManager.startTest("Valid login Test"); // -- This has been implemented in Testlistener class
		System.out.println("Running test method 1 on thread: " +Thread.currentThread().getId());
		ExtendManager.logStep("Navigating to Login page entering username and password");
		loginpage.login(Username, Password);
		ExtendManager.logStep("Verifying admin tab is visible or not");
		softAssert.assertTrue(homepage.isAdminTabVisible(),"Admin tab should be visible after successful login");
		ExtendManager.logStep("Validation successfully");
		homepage.logout();
		ExtendManager.logStep("Logged out successfully");
		staticWait(2);
	}
	
	@Test(dataProvider="InValidLoginData", dataProviderClass = DataProviders.class)
	public void inValidLoginTest(String Username, String Password)
	{
		ExtendManager.startTest("InValid login Test");  //-- This has been implemented in Testlistener class
		ExtendManager.logStep("Navigating to Login page entering username and password");
		loginpage.login(Username, Password);
		String exceptedErrorMessage = "Invalid credentials";
		ExtendManager.logStep("Verifying Expected error message");
		softAssert.assertTrue(loginpage.verifyErrorMessage(exceptedErrorMessage),"Test Failed : Invalid error message");
		ExtendManager.logStep("Validation successfully - Test is Passed");
		
		softAssert.assertAll();
	}
	
	
}
