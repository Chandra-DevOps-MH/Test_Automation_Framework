package com.orangehrm.test;

import org.testng.annotations.Test;

import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.utilities.ExtendManager;

public class DummyTest2 extends BaseClass{
	
	@Test
	public void dummyTest2() {
	
		   ExtendManager.startTest("Start the dummyTest1"); // -- This has been implemented in Testlistener class
			String Title = getDriver().getTitle();
			ExtendManager.logStep("Verifying the title");
			assert Title.equals("OrangeHRM"):" Title is not matching ";
			ExtendManager.logStep("Test passed - Test Passed - Title is matched"); 	
			System.out.println("Test Passed - Title is matched");
			
	}
}
