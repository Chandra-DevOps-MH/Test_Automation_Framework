package com.orangehrm.baseclass;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

import org.apache.commons.lang3.text.translate.NumericEntityUnescaper.OPTION;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.chromium.HasCdp;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.asserts.SoftAssert;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.utilities.ExtendManager;
import com.orangehrm.utilities.LoggerManager;

public class BaseClass {

	protected static Properties prop;
	//protected static WebDriver driver;
	//protected static ActionDriver actiondriver;
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	private static ThreadLocal<ActionDriver> actiondriver = new ThreadLocal<>();
	public static final Logger logger = LoggerManager.getLogger(BaseClass.class);
	
	protected ThreadLocal<SoftAssert> softAssert = ThreadLocal.withInitial(SoftAssert::new);
	
	//Getter method for Soft Assert
	public SoftAssert getsoftAssert()
	{
		return softAssert.get();
	}
	


	// Load the configuration file
	@BeforeSuite
	public void loadConfig() throws IOException {

		prop = new Properties();
		FileInputStream fis = new FileInputStream(System.getProperty("user.dir")+  "/src/main/resources/config.properties");
		prop.load(fis);
		logger.info("config.properties file loaded");
		
		//Start the extent report
		ExtendManager.getReporter();// -- This has been implemented in TestListener class
	}

	@BeforeMethod
	public synchronized void setUp() throws IOException {
		System.out.println("Setting up WebDriver for :" + this.getClass().getSimpleName());
		launchBrowser();
		configureBrowser();
		staticWait(2);
		logger.info("WebDriver initialized and Browser maximized");
		
		//Initialize the action driver only once
		/*if(actiondriver == null)
		{
			actiondriver = new ActionDriver(driver);
			logger.info("Action driver instance is created: " +Thread.currentThread().getId());
		} */
		
		actiondriver.set(new ActionDriver(getDriver()));
		logger.info("Action driver instance is created: " +Thread.currentThread().getId());
	}

	// Initialize the WebDriver based on browser defined in config.properties file
	private synchronized void launchBrowser() {

		String browser = prop.getProperty("browser");

		if (browser.equalsIgnoreCase("chrome")) {
			
			//create ChromeOptions
			ChromeOptions options = new ChromeOptions(); 
			options.addArguments("--headless"); //Run Chrome in headless mode
			options.addArguments("--disable-gpu"); //Disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); //Set window size
			options.addArguments("--disable-notifications"); //Disable browser notification
			options.addArguments("--no-sandbox"); //Required in some Linux enviroments
			options.addArguments("--disable-dev-shm-usage"); //Resolve issue in resources
			
			
			
			//driver = new ChromeDriver();
			driver.set(new ChromeDriver(options)); //New changes as per thread
			
			Map<String, Object> metrics = new HashMap<>();
			metrics.put("width", 1920);
			metrics.put("height", 1080);
			metrics.put("deviceScaleFactor", 1);
			metrics.put("mobile", false);

			((ChromiumDriver) BaseClass.getDriver()).executeCdpCommand("Emulation.setDeviceMetricsOverride", metrics);
			
			ExtendManager.registerDriver(getDriver());
			logger.info("Chrome driver instance is created");
		} else if (browser.equalsIgnoreCase("firefox")) {
			
			//create FireFoxOptions
			FirefoxOptions options = new FirefoxOptions(); 
			options.addArguments("--headless"); //Run Firefox in headless mode
			options.addArguments("--disable-gpu"); //Disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); //Set window size
			options.addArguments("--disable-notifications"); //Disable browser notification
			options.addArguments("--no-sandbox"); //Required in some Linux enviroments
			options.addArguments("--disable-dev-shm-usage"); //Resolve issue in resources
			
			//driver = new FirefoxDriver();
			driver.set(new FirefoxDriver(options));  //New changes as per thread
			
			Map<String, Object> metrics = new HashMap<>();
			metrics.put("width", 1920);
			metrics.put("height", 1080);
			metrics.put("deviceScaleFactor", 1);
			metrics.put("mobile", false);

			((ChromiumDriver) BaseClass.getDriver()).executeCdpCommand("Emulation.setDeviceMetricsOverride", metrics);
			
			ExtendManager.registerDriver(getDriver());
			logger.info("FireFox driver instance is created");
		} else if (browser.equalsIgnoreCase("edge")) {
			
			
			//create EdgeOpetions
			EdgeOptions options = new EdgeOptions(); 
			options.addArguments("--headless"); //Run Edge in headless mode
			options.addArguments("--disable-gpu"); //Disable GPU for headless mode
			options.addArguments("--window-size=1920,1080"); //Set window size
			options.addArguments("--disable-notifications"); //Disable browser notification
			options.addArguments("--no-sandbox"); //Required in some Linux enviroments
			options.addArguments("--disable-dev-shm-usage"); //Resolve issue in resources
			
			//driver = new EdgeDriver();
			driver.set(new EdgeDriver(options));
			
			Map<String, Object> metrics = new HashMap<>();
			metrics.put("width", 1920);
			metrics.put("height", 1080);
			metrics.put("deviceScaleFactor", 1);
			metrics.put("mobile", false);

			((ChromiumDriver) BaseClass.getDriver()).executeCdpCommand("Emulation.setDeviceMetricsOverride", metrics);
			ExtendManager.registerDriver(getDriver());
			logger.info("Edge driver instance is created");
		} else {

			throw new IllegalArgumentException("Browser not supportes" + browser);
		}

	}

	// configure browser settings such as implicit wait, maximize the browser,
	// navigate to the URL
	private void configureBrowser() {
		// Implicit wait
		int implicitwait = Integer.parseInt(prop.getProperty("implicitWait"));
	    getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitwait));

		// Maximize the browser
	    getDriver().manage().window().maximize();

		// Navigate to URL
		try {
			getDriver().get(prop.getProperty("url"));
		} catch (Exception e) {

			System.out.println("Failed to navigate to the URL" + e.getMessage());
		}
	}

	@AfterMethod
	public synchronized void tearDown() {
		if (getDriver() != null) {
			try {
				getDriver().quit();
			} catch (Exception e) {
				System.out.println("Unable to quit the driver: " +e.getMessage());
			}
		}
		logger.info("WebDriver instance is closed");
		//driver = null;
		//actiondriver = null;
		driver.remove();
		actiondriver.remove();
		
		ExtendManager.endTest(); //--This has been implemented in TestListener

	}
	
	//Getter method for prop
	public static Properties getProp()
	{
		return prop;
	}
	public void setDriver(ThreadLocal<WebDriver> driver)
	{
		this.driver = driver;
	}
	//Driver getter method
	/*public WebDriver getDriver()
	{
		return driver;
	}
	*/
	//Driver setter method
	 
	
	//Getter method for webdriver
	public static WebDriver getDriver()
	{
		if(driver.get() == null)
		{
			System.out.println("WebDriver is not initialized");
			throw new IllegalStateException("WebDriver is not initialized");
		}
		return driver.get();
		
	}
	
	//Getter mathod for Actiondriver
	public static ActionDriver getActionDriver()
	{
		if(actiondriver.get() == null)
		{
			System.out.println("ActionDriver is not initialized");
			throw new IllegalStateException("ActionDriver is not initialized");
		}
		return actiondriver.get();
		
	}

	// Static wait for pause
	public void staticWait(int seconds)
	{
		LockSupport.parkNanos(TimeUnit.SECONDS.toNanos(seconds));
	}

}
