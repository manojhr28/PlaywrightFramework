package org.example.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class UiValidationContinuedTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright= Playwright.create();
        browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page=browser.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");

    }

    @Test(groups={"smoke"})
    public void uiValidationContinued(){
        //Validate hidden field
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();

        //Dialog box
        page.onDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Alert")).click();

        //Mouse Hover -> just to move cursor on to the button and check if the options are available.
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
//        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Top")).click();

        //Frames
        FrameLocator framePage=page.frameLocator("#courses-iframe");
        framePage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Learning paths")).click();
        String textCheck=framePage.getByRole(AriaRole.HEADING, new FrameLocator.GetByRoleOptions().setName("LEARNING PATHS")).innerText();
        System.out.println(textCheck);

    }

    @Test
    public void screenShot(){
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("pagescreenshot.jpg")));
        Locator textBox=page.getByPlaceholder("Hide/Show Example");
        textBox.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("textBox.jpg")));
        page.locator("#hide-textbox").click();
        page.screenshot(new  Page.ScreenshotOptions().setPath(Paths.get("screenshotPostClick.jpg")));

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        page.close();
        browser.close();
        playwright.close();
    }
}
