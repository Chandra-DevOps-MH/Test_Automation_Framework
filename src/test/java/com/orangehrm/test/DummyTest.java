package com.orangehrm.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.utilities.ExtendManager;

public class DummyTest extends BaseClass{
	
	@Test
	public void dummyTest() {
		
			//Some changes to check jenkins build auto run when we push code
		    ExtendManager.startTest("Start the dummyTest"); //-- This has been implemented in Testlistener class
			String Title = getDriver().getTitle();
			ExtendManager.logStep("Verifying the OrangeHRM title");
			assert Title.equals("OrangeHRM"):" Title is not matching ";
			System.out.println("Test Passed - Title is matched");
			ExtendManager.logStep("Skipping the test as part of testing");
			throw new SkipException("Skipping the test as part of testing");
	}
}
