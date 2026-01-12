package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.baseclass.BaseClass;

public class LogInPage {
	
		
		private ActionDriver actiondriver;
		
		//define locators using By class
		private By usernameField = By.name("username");
		private By passwordField = By.cssSelector("input[type = 'password']");
		private By loginbtn = By.xpath("//button[text() = ' Login ']");
		private By errorMessage = By.xpath("//p[text() = 'Invalid credentials']");
		
		//Initialize the ActionDriver object by passing the WebDriver instance
		/*public LogInPage(WebDriver driver)
		{
			this.actiondriver = new ActionDriver(driver);
		}*/
		
		public LogInPage(WebDriver driver)
		{
			this.actiondriver = BaseClass.getActionDriver();
		}
		
		//Method to perform Login
		public void login(String username, String password)
		{
			actiondriver.enterText(usernameField, username);
			actiondriver.enterText(passwordField,password);
			actiondriver.click(loginbtn);
			
		}
		
		//Method to check if error message displayed
		public boolean isErrorMessageDisplayed()
		{
			return actiondriver.isDisplayed(errorMessage);
		}
		
		//Method to get text from error message
		public String getErrorMessageText()
		{
			return actiondriver.getText(errorMessage);
		}
		
		//Verify if error is correct or not
		public boolean verifyErrorMessage(String expectederrormessage)
		{
			return actiondriver.compareText(errorMessage, expectederrormessage);
		}

}
