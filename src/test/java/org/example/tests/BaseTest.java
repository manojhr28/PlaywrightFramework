package org.example.tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class BaseTest {

    Playwright playwright;
    Browser browser;
    Page page;
    String baseUrl;

    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {

        Properties prop=new Properties();
        FileInputStream fis=new FileInputStream("src/test/resources/config.properties");
        prop.load(fis);

        String browserName=System.getProperty("browser")!=null? System.getProperty("browser"):prop.getProperty("browser");
//        String browserName=prop.getProperty("browser");


        playwright=Playwright.create();

        if(browserName.equals("firefox")){
            browser=playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false)); //to run test on firefox engine
        }
        else if(browserName.equals("safari")){
            browser=playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false)); // to run test on webkit (Safari) engine
        }
        else{
//            browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browser=playwright.chromium().launch();//to run test on chrome engine
        }

        page= browser.newPage();
        PlaywrightAssertions.setDefaultAssertionTimeout(6000); //increase the wait time from 5 to 6 seconds on all testcases

        baseUrl=prop.getProperty("url");

//        Runs test on actual browser (replace value in setChannel() as msedge and firefox to run test in respective browsers)
//        Browser browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        page.close();
        browser.close();
        playwright.close();
    }
}
