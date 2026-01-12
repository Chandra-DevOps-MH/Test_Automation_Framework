package com.orangehrm.actiondriver;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.utilities.ExtendManager;

public class ActionDriver {

	protected WebDriver driver;
	protected WebDriverWait wait;
	public static final Logger logger = BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver = driver;
		int explicitWait = Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait = new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
	}

	// Method to click an element
	public void click(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitforElementToBeClickable(by);
			applyBorder(by, "green");
			driver.findElement(by).click();
			ExtendManager.logStep("Clicked an element -->" + elementDescription);
			logger.info("Clicked an element -->" + elementDescription);
		} catch (Exception e) {
			System.out.println("Unable to click element:" + e.getMessage());
			applyBorder(by, "red");
			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to click element",
					elementDescription + "Unable to click an element");
			logger.info("Unable to click element: " + e.getMessage());

		}
	}

	// Method to click an element by using JSexecutor
	public void Jsclick(By by) {
		String elementDescription = getElementDescription(by);
		try {
			waitforElementToBeVisible(by);
			scrollToElement(by);
			waitforElementToBeClickable(by);
			applyBorder(by, "green");
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].click()", driver.findElement(by));
			ExtendManager.logStep("Clicked an element -->" + elementDescription);
			logger.info("Clicked an element -->" + elementDescription);
		} catch (Exception e) {
			System.out.println("Unable to click element:" + e.getMessage());
			applyBorder(by, "red");
			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to click element",
					elementDescription + "Unable to click an element");
			logger.info("Unable to click element: " + e.getMessage());

		}
	}

	// Method to enter an text into an input field
	public void enterText(By by, String value) {
		try {
			waitforElementToBeVisible(by);
			// driver.findElement(by).clear();
			// driver.findElement(by).sendKeys(value);
			WebElement element = driver.findElement(by);
			applyBorder(by, "green");
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text on: " + getElementDescription(by) + " Entered value is: " + value);

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to enter the text: " + e.getMessage());
		}
	}

	// Method to get text from an input field
	public String getText(By by) {
		try {
			waitforElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Get text from " + getElementDescription(by));
			return driver.findElement(by).getText();
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to get text" + e.getMessage());
			return "";
		}
	}

	// Method to compare two values
	public boolean compareText(By by, String ExpectedValue) {
		try {
			waitforElementToBeVisible(by);
			String ActualValue = driver.findElement(by).getText();
			if (ActualValue.equals(ExpectedValue)) {
				logger.info("Text are matching: " + ActualValue + " equals " + ExpectedValue);
				applyBorder(by, "green");
				ExtendManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text",
						"Text verified Successfully " + ActualValue + " equals " + ExpectedValue);
				return true;

			} else {
				logger.error("Text are not matching: " + ActualValue + " not equals " + ExpectedValue);
				applyBorder(by, "red");
				ExtendManager.logFailure(BaseClass.getDriver(), "Compare Text",
						"Text Comparison Failed" + ActualValue + " not equals" + ExpectedValue);

				return false;
			}
		} catch (Exception e) {

			logger.error("Unable to comapare text" + e.getMessage());
			return false;
		}

	}

	// Method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitforElementToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element is displayed " + getElementDescription(by));
			ExtendManager.logStep("Element is displayed " + getElementDescription(by));
			ExtendManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed",
					"Element is displayed: " + getElementDescription(by));
			return driver.findElement(by).isDisplayed();

		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Element is not displayed" + e.getMessage());
			ExtendManager.logFailure(BaseClass.getDriver(), "Element is not displayed ",
					"Element is not displayed: " + getElementDescription(by));
			return false;
		}
	}

	// Wait for page load
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readystate").equals("complete"));
			logger.info("Page loaded successfully");
		} catch (Exception e) {
			logger.error("Page did not load within" + timeOutInSec + " seconds.exception" + e.getMessage());
		}
	}

	// Scroll to an element
	public void scrollToElement(By by) {
		try {
			JavascriptExecutor js = (JavascriptExecutor) driver;
			WebElement element = driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true)", element);
			applyBorder(by, "green");
		} catch (Exception e) {

			logger.info("Unable to locate element" + e.getMessage());
		}
	}

	// Wait for element to be clickable
	private void waitforElementToBeClickable(By by) {
		try {
			wait.until(ExpectedConditions.elementToBeClickable(by));
		} catch (Exception e) {
			logger.info("Element is not clickable: " + e.getMessage());
		}
	}

	// Wait for element to be visible
	private void waitforElementToBeVisible(By by) {
		try {
			wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		} catch (Exception e) {

			logger.info("Element is not visible: " + e.getMessage());
		}
	}

	// Method to get the description of an element using by locator
	public String getElementDescription(By locator) {
		if (driver == null) {
			return "driver is null";
		}
		if (locator == null) {
			return "locator is null";
		}

		try {
			// find the element using the locator
			WebElement element = driver.findElement(locator);

			// Get element attributes
			String name = element.getDomAttribute("name");
			String id = element.getDomAttribute("id");
			String text = element.getText();
			String ClassName = element.getDomAttribute("class");
			String placeholder = element.getDomAttribute("placeholder");

//Return the description based on element attribute	
			if (isNotEmpty(name)) {
				return "Element with name " + name;
			} else if (isNotEmpty(id)) {
				return "Element with id " + id;

			} else if (isNotEmpty(text)) {
				return "Element with text " + truncate(text, 50);

			} else if (isNotEmpty(ClassName)) {
				return "Element with Class" + ClassName;

			} else if (isNotEmpty(placeholder)) {
				return "Element with Placeholder " + placeholder;
			}
		} catch (Exception e) {
			logger.error("Unable to describe the element " + e.getMessage());

		}
		return "Unable to describe the element";

	}

	// Utility method to check String is not null or empty
	public boolean isNotEmpty(String value) {
		return value != null && !value.isEmpty();
	}

	// Utility method to check long String
	public String truncate(String value, int maxLength) {
		if (value == null || value.length() <= maxLength) {
			return value;
		}

		return value.substring(0, maxLength);
	}

	// Utility method to border an element
	public void applyBorder(By by, String color) {
		try {
			// Locate the element
			WebElement element = driver.findElement(by);

			// Apply the border
			String script = "arguments[0].style.border='3px solid " + color + "'";
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript(script, element);
			logger.info("Applied the border with color " + color + " to element " + getElementDescription(by));
		} catch (Exception e) {

			logger.warn("failed to apply border to an element " + getElementDescription(by));
		}
	}

	// ============================Select Methods===========================

	// Method to select a dropdown by visible text
	public void selectByVisibleText(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByVisibleText(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value" + element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to select dropdown value" + value + " " + e);
		}
	}

	// Method to select a dropdown by value
	public void selectByVisiblevalue(By by, String value) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByValue(value);
			applyBorder(by, "green");
			logger.info("Selected dropdown value" + element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to select dropdown value" + value + " " + e);
		}
	}

	// Method to select a index by value
	public void selectByIndex(By by, int index) {
		try {
			WebElement element = driver.findElement(by);
			new Select(element).selectByIndex(index);
			applyBorder(by, "green");
			logger.info("Selected dropdown value" + element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.info("Unable to select dropdown value of index" + index + " " + e);
		}
	}

	// Mathod to get all options from a dropdown
	public List<String> getDropDownOptions(By by) {
		List<String> optionsList = new ArrayList<>();

		try {
			WebElement dropdownElement = driver.findElement(by);
			Select select = new Select(dropdownElement);
			for (WebElement options : select.getOptions()) {
				optionsList.add(options.getText());
			}
			applyBorder(by, "green");
			logger.info("Retrieved dropdown options for " + getElementDescription(by));

		} catch (Exception e) {
			applyBorder(by, "Red");
			logger.error("Unable to get dropdown options " + e.getMessage());
		}

		return optionsList;

	}

	// Method to scroll to the bottom of the page
	public void scrollToBottom() {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		logger.info("Scrolled to the bottom of the page");

	}

	// Mathod to highlight element using Javascript
	public void highlightElement(By by) {
		try {
			WebElement element = driver.findElement(by);
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("arguments[0].setAttribute('style', 'background:yellow; border: 2px solid red')", element);
			logger.info("Highlighted element using javascript");
		} catch (Exception e) {

			logger.info("Unable to highlight element using javascript ", e);
		}
	}

	// ==========================window and frame
	// Handling============================
	public void switchToWindow(String windowTitle) {
		try {
			Set<String> windows = driver.getWindowHandles();
			for (String window : windows) {
				driver.switchTo().window(window);
				if (driver.getTitle().equals(windowTitle)) {
					logger.info("Switched to window: " + windowTitle);
					return;
				}
			}

		} catch (Exception e) {
			logger.info("Unable to switch window", e);
		}
	}

	// Mathod to switch to an iframe
	public void switchToFrame(By by) {
		try {
			WebElement element = driver.findElement(by);
			driver.switchTo().frame(element);
			logger.info("Switched to iframe " + getElementDescription(by));
		} catch (Exception e) {
			logger.error("Unable to switch to iframe ", e);
		}
	}

	// Method to switch back to the default content
	public void switchToDefaultContent() {
		driver.switchTo().defaultContent();
		logger.info("Switched back to default content. ");
	}

	// ====================Alert Handling==============================

	// Method to accept an alert popup
	public void acceptAlert() {
		try {
			driver.switchTo().alert().accept();
			logger.info("Alert accepted");
		} catch (Exception e) {

			logger.error("No alert found to accept ", e);
		}
	}

	// Method to dismiss an alert popup
	public void dismissAlert() {
		try {
			driver.switchTo().alert().dismiss();
			logger.info("Alert dismissed");

		} catch (Exception e) {
			logger.error("");
		}
	}

	// Method to get a text from popup
	public String getTextFromPopUp() {
		try {

			return driver.switchTo().alert().getText();

		} catch (Exception e) {
			logger.error("No alert text found ", e);
			return "";
		}
	}

	// ===================Browser actions===========================

	// Method to refresh page
	public void refreshPage() {
		try {
			driver.navigate().refresh();
			ExtendManager.logStep("Page refreshed successfully");
			logger.info("Page refreshed successfully");
		} catch (Exception e) {
			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to refresh page", "Unable to refresh page");
			logger.error("Unable to refresh page: " + e.getMessage());
		}

	}

	// Method get a current URL
	public String getCurrentURL() {
		try {
			String URL = driver.getCurrentUrl();
			ExtendManager.logStep("Current URL fetched " + URL);
			logger.info("Current URL fetched " + URL);
			return URL;
		} catch (Exception e) {

			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to fetch current URL",
					"Unable to fetch current URL");
			logger.error("Unable to fetch current URL " + e.getMessage());
			return null;
		}

	}

	// Method to maximize window
	public void maximizeWindow() {
		try {
			driver.manage().window().maximize();
			ExtendManager.logStep("Browser window maximized");
			logger.info("Browser window maximized");

		} catch (Exception e) {

			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to maximize window", "Unable to maximize");
			logger.error("Unable to maximize window" + e.getMessage());

		}

	}
	
	//===================== Advanced WebElement Actions=======================
	
	//Method to move to element
	public void moveToElement(By by)
	{
		String elementDescription = getElementDescription(by);
		
		try {
			Actions action  = new Actions(BaseClass.getDriver());
			WebElement element = driver.findElement(by);
			action.moveToElement(element).perform();
			ExtendManager.logStep("Moved to element " +elementDescription);
			logger.info("Moved to element " +elementDescription);
		} catch (Exception e) {
			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to move element", "Unable to move element");
		}
		
	}
	
	//Method to drag and drop element
	public void dragAndDrop(By source, By target)
	{
		String sourceDescription = getElementDescription(source);
		String targetDescription = getElementDescription(target);
		
		try {
			Actions action = new Actions(BaseClass.getDriver());
			WebElement sourceDestination = driver.findElement(source);
			WebElement targetDestination = driver.findElement(target);
			action.dragAndDrop(sourceDestination, targetDestination).perform();;
			ExtendManager.logStep("Dragged element: " +sourceDescription+ " and dropped on " +targetDescription);
			logger.info("Dragged element: " +sourceDescription+ " and dropped on " +targetDescription);
		} catch (Exception e) {
			ExtendManager.logFailure(BaseClass.getDriver(), sourceDescription, targetDescription);
			
					}
		
	}
	
	// Method to perform double click
	public void doubleClick(By by)
	{
		String elementDescription = getElementDescription(by);
		
		try {
			Actions action = new Actions(BaseClass.getDriver());
			WebElement element = driver.findElement(by);
			action.doubleClick(element).perform();
			ExtendManager.logStep("Double clicked on element" +elementDescription);
			logger.info("Double clicked on element" +elementDescription);
		} catch (Exception e) {
			ExtendManager.logFailure(BaseClass.getDriver(), "Unable to double click on element", "Unable to double click on element");
			logger.info("nable to double click on element" +e.getMessage());
		}
		
		
	}
	
	// Method to perform right click
		public void rightClick(By by)
		{
			String elementDescription = getElementDescription(by);
			
			try {
				Actions action = new Actions(BaseClass.getDriver());
				WebElement element = driver.findElement(by);
				action.contextClick(element).perform();
				ExtendManager.logStep("Right clicked on element" +elementDescription);
				logger.info("Right clicked on element" +elementDescription);
			} catch (Exception e) {
				ExtendManager.logFailure(BaseClass.getDriver(), "Unable to right click on element", "Unable to right click on element");
				logger.info("Unable to right click on element" +e.getMessage());
			}
			
			
		}
	
		//Method to sendkeys with actions
		public void sendKeysWithActions(By by, String value)
		{
			String elementDescription = getElementDescription(by);
			
			try {
				Actions action = new Actions(BaseClass.getDriver());
				WebElement element = driver.findElement(by);
				action.sendKeys(element, value).perform();
				ExtendManager.logStep("Sent keys to element: " +elementDescription+ " Value: " +value);
				logger.info("Sent keys to element: " +elementDescription+ " Value: " +value);
			} catch (Exception e) {
				ExtendManager.logFailure(BaseClass.getDriver(), "Unable to send keys to element", "Unable to send keys to element");
				logger.info("Unable to send keys to element" +e.getMessage());
			}
			
			
		}
		
		//Method to clear the text
				public void clearText(By by)
				{
					String elementDescription = getElementDescription(by);
					
					try {
						
						WebElement element = driver.findElement(by);
						 element.clear();
						ExtendManager.logStep("Cleared text in element: " +elementDescription);
						logger.info("Cleared text in element: " +elementDescription);
					} catch (Exception e) {
						ExtendManager.logFailure(BaseClass.getDriver(), "Unable to clear text", "Unable to clear text");
						logger.info("Unable to clear text" +e.getMessage());
					}
					
					
				}
				
				//Method to upload file
				public void uploadFile(By by, String filePath)
				{
					String elementDescription = getElementDescription(by);
					
					try {
						
						WebElement element = driver.findElement(by);
						 element.sendKeys(filePath);
						 applyBorder(by, "green");
					logger.info("Upload file: " +elementDescription);
					} catch (Exception e) {
						applyBorder(by, "Red");
						logger.error("Unable to upload file: " +e.getMessage());
					}
					
					
				}


}
