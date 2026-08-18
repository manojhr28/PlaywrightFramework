package org.example.tests;


import com.microsoft.playwright.Locator;
import org.example.pages.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CreateEventTest extends BaseTest{


    @Test(description = "Login to the website, create event and book the event")
    public void createEvent() throws InterruptedException {

        String title="Playwright Framework";
        //Login
        LoginPage loginPage=new LoginPage(page,baseUrl);
        loginPage.login("unknown1@gmail.com","Unknown@1");

        //Land into home page and verify
        HomePage homePage=new HomePage(page);
        homePage.validateHomePage();

        //Create new Event
        AdminEventPage adminEventPage=new AdminEventPage(page);
        adminEventPage.createEvent(title,
                "I am doing Playwright Framework",
                "Workshop",
                "Bengaluru",
                "Bengaluru Conference room",
                "2026-08-29T10:00",
                "100",
                "100");

        //Navigate to event page
        EventPage eventPage=new EventPage(page);
        eventPage.goTo();
        Locator myEventCard=eventPage.findMyEventCard(title);
        int seatCountBefore=eventPage.getSeatCount(myEventCard);
        eventPage.proceedToBookEvent(myEventCard);


        //Navigate to Book event
        BookEventPage bookeventPage=new BookEventPage(page);
        bookeventPage.validateBookEventPage();
        bookeventPage.proceedToBookEventAndConfirm("John",
                "unknown1@gmail.com",
                "9087654321");
        String bookingRef= bookeventPage.getBookingRef();

        //Validate my booking page by verifying the booking ref
        MyBookingsPage myBookingspage=new MyBookingsPage(page);
        myBookingspage.validateMyBookings(bookingRef);

        //Validate the seat count as decreased
        String seatCountAfterBooking=eventPage.getSeatCountAfterBooking(title);

        //Compare the seat count before and after booking
        eventPage.compareSeatCount(seatCountAfterBooking,seatCountBefore);

    }


}
