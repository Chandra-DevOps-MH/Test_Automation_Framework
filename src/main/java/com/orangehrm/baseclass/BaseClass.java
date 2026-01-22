package com.orangehrm.baseclass;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import org.testng.asserts.SoftAssert;

import com.beust.jcommander.Parameter;
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
	@Parameters("browser")
	public synchronized void setUp(String browser) throws IOException {
		System.out.println("Setting up WebDriver for :" + this.getClass().getSimpleName());
		launchBrowser(browser);
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
	private synchronized void launchBrowser(String browser) {
	    boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
	    String getURL = prop.getProperty("gridURL");

	    try {
	        if (seleniumGrid) {
	            // ---------- Selenium Grid ----------
	            if (browser.equalsIgnoreCase("chrome")) {
	                ChromeOptions options = new ChromeOptions();
	                options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080",
	                        "--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage", "--incognito");
	                driver.set(new RemoteWebDriver(new URL(getURL), options));
	                
	                
	            } else if (browser.equalsIgnoreCase("firefox")) {
	                FirefoxOptions options = new FirefoxOptions();
	                options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080",
	                        "--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage");
	                driver.set(new RemoteWebDriver(new URL(getURL), options));

	            } else if (browser.equalsIgnoreCase("edge")) {
	                EdgeOptions options = new EdgeOptions();
	                options.addArguments("--headless");
	                options.addArguments("--window-size=1920,1080");

	                driver.set(new RemoteWebDriver(new URL(getURL), options));
	            } else {
	                throw new IllegalArgumentException("Browser not supported for Grid: " + browser);
	            }
	            logger.info("Remote WebDriver created for Grid in headless mode");

	        } else {
	            // ---------- Local Browser ----------
	            if (browser.equalsIgnoreCase("chrome")) {
	                ChromeOptions options = new ChromeOptions();
	                options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080",
	                        "--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage", "--incognito");
	                driver.set(new ChromeDriver(options));
	                
	              	            } else if (browser.equalsIgnoreCase("firefox")) {
	                FirefoxOptions options = new FirefoxOptions();
	                options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080",
	                        "--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage");
	                driver.set(new FirefoxDriver(options));

	            } else if (browser.equalsIgnoreCase("edge")) {
	                EdgeOptions options = new EdgeOptions();
	                options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080",
	                        "--disable-notifications", "--no-sandbox", "--disable-dev-shm-usage");
	                driver.set(new EdgeDriver(options));

	            } else {
	                throw new IllegalArgumentException("Browser not supported locally: " + browser);
	            }
	            logger.info("Local WebDriver instance created");
	        }

	     // Execute CDP ONLY for Chrome
	        if (browser.equalsIgnoreCase("chrome")) {
	            Map<String, Object> metrics = new HashMap<>();
	            metrics.put("width", 1920);
	            metrics.put("height", 1080);
	            metrics.put("deviceScaleFactor", 1);
	            metrics.put("mobile", false);

	            if (getDriver() instanceof HasCdp) {
	                ((HasCdp) getDriver())
	                    .executeCdpCommand("Emulation.setDeviceMetricsOverride", metrics);
	            }
	        }

	        // Register driver with ExtendManager
	        ExtendManager.registerDriver(getDriver());

	    } catch (MalformedURLException e) {
	        throw new RuntimeException("Invalid Grid URL", e);
	    }
	}

	// configure browser settings such as implicit wait, maximize the browser,
	// navigate to the URL
	private void configureBrowser() {
		// Implicit wait
		int implicitwait = Integer.parseInt(prop.getProperty("implicitWait"));
		boolean seleniumGrid = Boolean.parseBoolean(prop.getProperty("seleniumGrid"));
		getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(implicitwait));
	    

		// Maximize the browser
	    getDriver().manage().window().maximize();

		/*
		 * // Navigate to URL try { getDriver().get(prop.getProperty("url")); } catch
		 * (Exception e) {
		 * 
		 * System.out.println("Failed to navigate to the URL" + e.getMessage()); }
		 */
	    
	    if(seleniumGrid)
	    {
	    	getDriver().get(prop.getProperty("url_grid"));
	    }else {
	    	getDriver().get(prop.getProperty("url_local"));
	 	   
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
