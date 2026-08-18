package org.example.tests;


import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class SampleTest {

    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        playwright= Playwright.create();
        browser=playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page=browser.newPage();

    }
    @Test(description = "Login to the website, create event and book the event")
    public void createEvent() throws InterruptedException {
        //https://eventhub.rahulshettyacademy.com/login
        //unknown1@gmail.com & unknown@1

        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder("you@email.com").fill("unknown1@gmail.com");
        page.getByLabel("Password").fill("Unknown@1");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();

        //Create new Event
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Manage Events")).nth(1).click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("+ New Event"))).isVisible();

        page.getByPlaceholder("Event title").fill("My Event");
        page.getByPlaceholder("Describe the event…").fill("I am doing my Event");
        page.getByLabel("Category").selectOption("Workshop");
        page.locator("#city").fill("Bengaluru");
        page.getByLabel("Venue").fill("Bengaluru Conference room");
        page.getByLabel("Event Date & Time").fill("2026-08-29T10:00");
        page.getByLabel("Price ($)").fill("100");
        page.getByLabel("Total Seats").fill("100");
        Thread.sleep(3000);
        page.locator("#add-event-btn").click();
        assertThat(page.getByText("Event created!")).isVisible();

        //Navigate to event page
        page.locator("#nav-events").click();
        Locator eventCard=page.getByTestId("event-card");
//        System.out.println("Total events: "+eventCard.count());
        Locator myEventCard=eventCard.filter(new Locator.FilterOptions().setHasText("My Event"));
        assertThat(myEventCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000)); //if specific locator takes time then use this option for specific locator to wait
        String seatCountBeforeBooking = myEventCard.getByText("seats available").innerText();
        int seatCountBefore=Integer.parseInt(seatCountBeforeBooking.split(" ")[0]);
        System.out.println("Seat count before booking: "+seatCountBefore);

        //Book an event
        myEventCard.getByTestId("book-now-btn").click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Book Tickets"))).isVisible();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
        page.getByPlaceholder("Your full name").fill("John");
        page.getByLabel("Email").fill("unknown1@gmail.com");
        page.getByLabel("Phone Number").fill("9087654321");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        String bookingRef=page.locator(".booking-ref").innerText();
        System.out.println("Booking ref: "+bookingRef);
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();

        Locator bookingCards=page.getByTestId("booking-card");
        Locator myBooking=bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(myBooking).isVisible();

        //Validate the seat count as decreased
        page.locator("#nav-events").click();
        page.waitForTimeout(1000);
        Locator eventCardAfterBooking=page.getByTestId("event-card");
        Locator myEventCardAfterBooking=eventCardAfterBooking.filter(new Locator.FilterOptions().setHasText("My Event"));
        String seatCountAfterBooking = myEventCardAfterBooking.getByText("seats available").innerText();
//        System.out.println("Number of seats: "+seatCountAfterBooking);


        //Compare the seat count
        int seatCountAfter=Integer.parseInt(seatCountAfterBooking.split(" ")[0]);
        System.out.println("Seat count after booking: "+seatCountAfter);
        Assert.assertTrue(seatCountBefore>seatCountAfter);

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(){
        page.close();
        browser.close();
        playwright.close();
    }


}
