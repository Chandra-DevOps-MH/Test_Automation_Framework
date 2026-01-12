package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {
	
	private static final String FILE_PATH = System.getProperty("user.dir")+ "/src/test/resources/testdata/TestData.xlsx";
	
	@DataProvider(name = "ValidLoginData")
	public static Object[][] validLoginData()
	{
		return getSheetData("ValidLoginTestData");
	}
	
	@DataProvider(name = "InValidLoginData")
	public static Object[][] inValidLoginData()
	{
		return getSheetData("InValidLoginTestData");
	}
	
	@DataProvider(name = "DBVerificationData")
	public static Object[][] DBVerificationData()
	{
		return getSheetData("DBVerificationTestData");
	}

	
	private static Object[][] getSheetData(String sheetName)
	{
		List<String[]> sheetData = ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
		
		Object[][] data = new Object[sheetData.size()][sheetData.get(0).length];
		
		for(int i = 0; i<sheetData.size();i++)
		{
			data[i] = sheetData.get(i);
		}
		
		return data;
	}
	

}
