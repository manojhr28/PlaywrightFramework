package org.example.tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
    }

    @Test(description = "Mock response and validate the Message in Event page")
    public void mockResponse() {

        page.getByPlaceholder("you@email.com").fill("unknown1@gmail.com");
        page.getByLabel("Password").fill("Unknown@1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();

        //Validate the message is present when we have events >=6
        page.route("**/api/events**",route -> route.fulfill(new Route.FulfillOptions().setPath(Paths.get("src/test/resources/get6Events.json"))));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards=page.locator("#event-card");
        assertThat(eventCards.first()).isVisible();
        Assert.assertEquals(eventCards.count(),6);
        assertThat(page.locator(".mx-1").first()).isVisible();

        //Validate the message is present when we have events <6
        page.route("**/api/events**",route -> route.fulfill(new Route.FulfillOptions().setPath(Paths.get("src/test/resources/get4Events.json"))));
        page.navigate("https://eventhub.rahulshettyacademy.com/events");

        Locator eventCards1 =page.locator("#event-card");
        assertThat(eventCards1.first()).isVisible();
        Assert.assertEquals(eventCards1.count(),4);
        assertThat(page.locator(".mx-1").first()).isHidden();


    }

    @Test(description = "Mock request and validate the the booking details is not accessed")
    public void mockRequest() throws InterruptedException {

        page.getByPlaceholder("you@email.com").fill("unknown1@gmail.com");
        page.getByLabel("Password").fill("Unknown@1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();

        //Validate booking details is not accessed
        page.locator("#nav-bookings").click();
        page.route("**/api/bookings/**",route -> route.resume(new Route.ResumeOptions().setUrl("\n" +
                        "https://api.eventhub.rahulshettyacademy.com/api/bookings/112573")));
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("View Details")).first().click();

        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Access Denied"))).isVisible();
        page.waitForTimeout(3000);


    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        page.close();
        browser.close();
        playwright.close();
    }

}
