package com.orangehrm.test;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.baseclass.BaseClass;
import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtendManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;
import junit.framework.Assert;


public class APITest{
	

	@Test(retryAnalyzer = RetryAnalyzer.class)
	public void verifyGetUserAPI()
	{
		SoftAssert softAssert = new SoftAssert();
		ExtendManager.startTest("API Validation Test Starts");
		//Step 1: Define API endpoint
		String endpoint = "https://jsonplaceholder.typicode.com/users/1";
		ExtendManager.logStep("API endpoint: "+endpoint);
		
		//Step 2: Send Get Request
		ExtendManager.logStep("Sending GET Request to the API");
		Response response = ApiUtility.sendGetRequest(endpoint);
		
		//Step 3: Validate status code
		ExtendManager.logStep("Validating API Response Status Code");
		boolean isStatusCodeValid = ApiUtility.validateStatusCode(response, 200);	
		
		softAssert.assertTrue(isStatusCodeValid, "Status code is not as Expected");
		
		if(isStatusCodeValid)
		{
			ExtendManager.logStepValidationForAPI("Status Code Validation Passed");
		}else
		{
			ExtendManager.logFailureAPI("Status Code Validation Failed");
		}
		
		//Step 4: Validate Username
		ExtendManager.logStep("Validating response body for username");
		String username = ApiUtility.getJsonValue(response, "username");
		boolean isUserNameValid = "Bret".equals(username);
		if(isUserNameValid)
		{
			ExtendManager.logStepValidationForAPI("Username validation Passed");
		}else {
			ExtendManager.logFailureAPI("Username validation failed");
			
		}
		
		//Step 5: Validate Email
				ExtendManager.logStep("Validating response body for Email");
				String email = ApiUtility.getJsonValue(response, "email");
				boolean isEmailValid = "Sincere@april.biz".equals(email);
				if(isEmailValid)
				{
					ExtendManager.logStepValidationForAPI("Email validation Passed");
				}else {
					ExtendManager.logFailureAPI("Email validation failed");
					
				}
				
		
				softAssert.assertAll();	
	}

}
