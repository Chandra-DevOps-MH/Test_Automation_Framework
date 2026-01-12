package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.baseclass.BaseClass;

public class HomePage {

	ActionDriver actiondriver;

	// Initialize the ActionDriver object by passing the WebDriver instance
	/*public HomePage(WebDriver driver) {
		this.actiondriver = new ActionDriver(driver);
	}*/
	
	public HomePage(WebDriver driver)
	{
		this.actiondriver = BaseClass.getActionDriver();
	}

	// Define locators using By class
	private By adminTab = By.xpath("//span[text() = 'Admin']");
	private By userIdButton = By.className("oxd-userdropdown-name");
	private By logoutBtn = By.xpath("//a[text() = 'Logout']");
	private By orangeHRMLogo = By.xpath("//div[@class = 'oxd-brand-banner']//img");
	private By pimTab = By.xpath("//span[text() = 'PIM']");
	private By employeeSearch = By.xpath("//label[text() = 'Employee Name']/parent::div/following-sibling::div/div/div/input");
	private By submitButton = By.xpath("//button[@type = 'submit']");
	private By empFirstandMiddleName = By.xpath("//div[@class = 'oxd-table-card']/div/div[3]");
	private By empLastName = By.xpath("//div[@class = 'oxd-table-card']/div/div[4]");
	
	//Method to verify admin tab is visible or not
	public boolean isAdminTabVisible()
	{
		return actiondriver.isDisplayed(adminTab);
	}
	
	//Method to verify Orange HRM logo
	public boolean isOrangeHRMlogoVisible()
	{
		return actiondriver.isDisplayed(orangeHRMLogo);
	}
	
	//Method to navigate to PIM tab
	public void clickOnPIMTab()
	{
		actiondriver.Jsclick(pimTab);
	}
	
	//Employee search
	public void employeeSearch(String value)
	{
		actiondriver.enterText(employeeSearch, value);
		actiondriver.click(submitButton);
		actiondriver.scrollToElement(empFirstandMiddleName);
		
	}
	
	//Verify employee first and middle name
	public boolean verifyEmployeeFirstAndMiddleName(String empFirstandMiddleNamefromDB)
	{
		return actiondriver.compareText(empFirstandMiddleName, empFirstandMiddleNamefromDB);	
		
	}
	
	//Verify employee Last name
		public boolean verifyEmployeeLastName(String empLastNamefromDB)
		{
			return actiondriver.compareText(empLastName, empLastNamefromDB);	
			
		}
		
	
	
	//Method to perform logout action
	public void logout()
	{
		actiondriver.click(userIdButton);
		actiondriver.click(logoutBtn);
	}
	

}
