package org.example.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ChildWindowHandle {
    Playwright playwright;
    Browser browser;
    BrowserContext context;
    Page page;
//    BrowserContext browserContextA;
//    Page pageA;

    @BeforeMethod(alwaysRun = true)
    public void setup(){
        playwright=Playwright.create();
        browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context=browser.newContext();
        context.tracing().start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        page=context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");

//        browserContextA=browser.newContext();
//        pageA=browserContextA.newPage();

    }

    @Test
    public void childWindowHandling(){

        Locator blinkingLink=page.locator(".blinkingText");
        Page newPage=context.waitForPage(()->blinkingLink.first().click());
        newPage.waitForLoadState();
        String childStr=newPage.locator(".red").textContent();
        String emailId=childStr.split(" ")[4];
//        System.out.println("Email Id: "+emailId);
        newPage.close();

        page.getByLabel("Username:").fill(emailId);
        System.out.println(page.getByLabel("Username:").inputValue());
    }

    @Test
    public void UiValidationTest(){

        Locator radioButton=page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName(" User"));
        radioButton.click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();
        Assert.assertTrue(radioButton.isVisible());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Consultant");


        Locator checkBox=page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
        checkBox.check();
        Assert.assertTrue(checkBox.isChecked());
    }
// whenever trace is enabled and zip file is created drop the zip file in url: https://trace.playwright.dev/
    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        page.close();
        context.tracing().stop(new Tracing.StopOptions().setPath(Paths.get("trace.zip")));
        context.close();
        browser.close();
        playwright.close();

    }
}
