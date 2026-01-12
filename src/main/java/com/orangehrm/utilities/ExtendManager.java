package com.orangehrm.utilities;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtendManager {
	
	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	private static Map<Long, WebDriver> driverMap = new HashMap<>();
	
	//Initialize the extent report 
	
	public synchronized static ExtentReports getReporter()
	{
		if(extent == null)
		{
			String reportPath = System.getProperty("user.dir")+"/src/test/resources/ExtentReport/ExtentReport.html";
			ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
			spark.config().setReportName("Automation Test Report");
			spark.config().setDocumentTitle("OrangeHRM Report");
			spark.config().setTheme(Theme.DARK);
			
			extent = new ExtentReports();
			
			extent.attachReporter(spark);
			//Adding system information
			extent.setSystemInfo("Operating System", System.getProperty("os.name"));
			extent.setSystemInfo("Java Version", System.getProperty("java.version"));
			extent.setSystemInfo("User Name", System.getProperty("user.name"));
			
			
		}
		return extent;
	}
	
	//Start the test
	public synchronized static ExtentTest startTest(String testName)
	{
		ExtentTest extentTest = getReporter().createTest(testName);
		test.set(extentTest);
		return extentTest;
		
	}
	
	//End a Test
	public synchronized static void endTest()
	{
		getReporter().flush();
	}
	
	//get Current Thread's Test
	public static ExtentTest getTest()
	{
		return test.get();
	}
	
	//Method to get the name of the current test
	public static String getTestName()
	{
		ExtentTest currentTest = getTest();
		if(currentTest != null)
		{
			return currentTest.getModel().getName();
		}else
		{
			 return "No test is currently active for this thread";
		}
	
	}
	
	//log a step
	public static void logStep(String logMessage)
	{
		 getTest().info(logMessage);
	}
	
	//Log a step validation with screenshot
	public static void logStepWithScreenshot(WebDriver driver, String logMessage, String ScreenshotMessage)
	{
		getTest().pass(logMessage);
		//Screenshot Method
		attachScreenshot(driver, ScreenshotMessage);
	}
	
	//Log a step validation for API
		public static void logStepValidationForAPI(String logMessage)
		{
			getTest().pass(logMessage);
			
		}
	
	//Log a Failure
	public static void logFailure(WebDriver driver, String logMessage, String ScreenshotMessage)
	{
		String colorMessage = String.format("<span style = 'color:red;'>%s</span>", logMessage);
		getTest().fail(colorMessage);
		//Screenshot Method
		attachScreenshot(driver, ScreenshotMessage);
	}
	
	//Log a Failure for API
		public static void logFailureAPI(String logMessage)
		{
			String colorMessage = String.format("<span style = 'color:red;'>%s</span>", logMessage);
			getTest().fail(colorMessage);
			
		}
	
	//Log a skip
	public static void logSkip(String logMessage)
	{
		String colorMessage = String.format("<span style = 'color:orange;'>%s</span>", logMessage);
		getTest().skip(colorMessage);
	}
	
	
	//Take a screenshot with date and time in the file
	public synchronized static String takeScreenshot(WebDriver driver, String screenshotName)
	{
		TakesScreenshot ts = (TakesScreenshot)driver;
		File src = ts.getScreenshotAs(OutputType.FILE);
		
		//Format date and time for file name
		String timeStamp = new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss").format(new Date());
		
		//Saving the screenshot to the file
		String destPath = System.getProperty("user.dir") + "/src/test/resources/screenshots/"+screenshotName+"_" +timeStamp+ ".png";
		
		File finalPath = new File(destPath);
		
		try {
			FileUtils.copyFile(src, finalPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		//Convert screenshot to base64 format
		String base64format = convertToBase64(src);
		return base64format;
	}
	
	//Convert screenshot to base64 format
	public static String convertToBase64(File screenshotFile)
	{
		String base64format = "";
		//Read the file content into the byte array
		byte[] fileContent;
		try {
			fileContent = FileUtils.readFileToByteArray(screenshotFile);
			base64format = Base64.getEncoder().encodeToString(fileContent);
			
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return base64format;
	}
	
	//Attach screenshot to report using Base64
	public static void attachScreenshot(WebDriver driver, String message)
	{
		try {
			String screenshotBase64 = takeScreenshot(driver, getTestName());
			getTest().info(message, com.aventstack.extentreports.MediaEntityBuilder.createScreenCaptureFromBase64String(screenshotBase64).build());
			
		}catch(Exception e)
		{
			getTest().fail("Failed to attach screenshot:" +message);
			e.printStackTrace();
		}
	}
	
	
	
	//Register webdriver for current thread
	public static void registerDriver(WebDriver driver)
	{
		driverMap.put(Thread.currentThread().getId(), driver);
	}

	

}
