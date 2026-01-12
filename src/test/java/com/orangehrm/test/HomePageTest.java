package com.orangehrm.test;

import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LogInPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtendManager;
import com.orangehrm.utilities.RetryAnalyzer;

import junit.framework.Assert;


public class HomePageTest extends BaseClass{
	
	private LogInPage loginpage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setupPages()
	{
		loginpage = new LogInPage(getDriver());
		homepage = new HomePage(getDriver());
	}
	
	
	@Test(dataProvider="ValidLoginData", dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String username, String password)
	{
	    ExtendManager.startTest("Home page Verify logo test");  //-- This has been implemented in Testlistener class
		ExtendManager.logStep("Navigating to Login page entering username and password");
		loginpage.login(username,password);
		ExtendManager.logStep("Verifying logo is visible or not");
		Assert.assertTrue(homepage.isOrangeHRMlogoVisible());
	    ExtendManager.logStep("Verification Successfull");
		homepage.logout();
		ExtendManager.logStep("Logged out Successfull");
		
	}

}
