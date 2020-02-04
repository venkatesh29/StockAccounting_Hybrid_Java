package DriverFactory;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

import CommonFunLibrary.FunctionLibrary;
import Utilities.ExcelFileUtil;

public class DriverScript {
	
	ExtentReports report;
	ExtentTest test;
	WebDriver driver;
	
  @Test
  public void startTest() throws Exception {
	  
	 //creating reference object for excel util methods
	  ExcelFileUtil excel=new ExcelFileUtil();
	  
	  for(int i=1;i<=excel.rowCount("MasterTestCases");i++){
		  String ModuleStatus="";
		  if(excel.getData("MasterTestCases", i, 2).equalsIgnoreCase("Y")){
			  
				//store module name into TCModule 
				String TCModule=excel.getData("MasterTestCases", i, 1);
				
				report=new ExtentReports("D:\\Batch81\\StockAccounting_Hybrid\\Reports\\"+TCModule+FunctionLibrary.generateDate()+".html");
				
				for(int j=1;j<=excel.rowCount(TCModule);j++)
				{
					
					test=report.startTest(TCModule);
					
					String Description=excel.getData(TCModule, j, 0);
					String Function_Name=excel.getData(TCModule, j, 1);
					String Locator_Type=excel.getData(TCModule, j, 2);
					String Locator_Value=excel.getData(TCModule, j, 3);
					String Test_Data=excel.getData(TCModule, j, 4);
					try{
							if(Function_Name.equalsIgnoreCase("startBrowser")){
								driver=FunctionLibrary.startBrowser(driver);
								test.log(LogStatus.INFO, Description);		
							}
							if(Function_Name.equalsIgnoreCase("openApplication")){
								FunctionLibrary.openApplication(driver);
								test.log(LogStatus.INFO, Description);
							}
							if(Function_Name.equalsIgnoreCase("typeAction")){
								FunctionLibrary.typeAction(driver, Locator_Type, Locator_Value, Test_Data);
								test.log(LogStatus.INFO, Description);
							}
							if(Function_Name.equalsIgnoreCase("clickAction")){
								FunctionLibrary.clickAction(driver,Locator_Type, Locator_Value);
								test.log(LogStatus.INFO, Description);
							}
							if(Function_Name.equalsIgnoreCase("waitForElement")){
								FunctionLibrary.waitForElement(driver, Locator_Type, Locator_Value, Test_Data);
								test.log(LogStatus.INFO, Description);
							}
							if(Function_Name.equalsIgnoreCase("closeBrowser")){
								FunctionLibrary.closeBrowser(driver);
								test.log(LogStatus.INFO, Description);
							}
							if(Function_Name.equalsIgnoreCase("tableValidation")){
								FunctionLibrary.tableValidation(driver, Test_Data);
								test.log(LogStatus.INFO, Description);
							}
							if(Function_Name.equalsIgnoreCase("captureData")){
								FunctionLibrary.captureData(driver, Locator_Type, Locator_Value);
								test.log(LogStatus.INFO, Description);
							}
							
							ModuleStatus="true";
							excel.setData(TCModule, j, 5, "PASS");
		
					}catch(Exception e){
						ModuleStatus="false";
						File srcFile=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
						FileUtils.copyFile(srcFile,new File("D:\\Batch81\\StockAccounting_Hybrid\\Screenshots\\"+Description+FunctionLibrary.generateDate()));
						excel.setData(TCModule, j,5,"FAIL");
						break;
					}
				}
			  if(ModuleStatus.equalsIgnoreCase("true")){
				  excel.setData("MasterTestCases", i, 3, "PASS");
			  }else if(ModuleStatus.equalsIgnoreCase("false")){
				  excel.setData("MasterTestCases", i, 3, "FAIL");
			  }
			  report.flush();
			  report.endTest(test);
		  }	  
		  else{
				//write as not Executed in status column in MasterTestCases sheet
				excel.setData("MasterTestCases", i, 3, "Not Executed");			
		 }
	  }  
  }
}
