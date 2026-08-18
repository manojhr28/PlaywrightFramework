package org.example.tests;


import com.microsoft.playwright.Locator;
import org.example.pages.*;
import org.example.utils.DataProviderUtil;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;

public class CreateEventDataDrivenTest extends BaseTest{


    @DataProvider(name="eventBookingData")
    public Object[][] getEventBookingData() throws IOException {
        return DataProviderUtil.getJsonDataToMap("/src/test/resources/eventBookingData.json");
    }

    @Test(dataProvider = "eventBookingData", description = "Login to the website, create event and book the event")
    public void createEvent(HashMap<String,String> data) throws InterruptedException {

        //Login
        LoginPage loginPage=new LoginPage(page,baseUrl);
        loginPage.login(data.get("email"),data.get("password"));

        //Land into home page and verify
        HomePage homePage=new HomePage(page);
        homePage.validateHomePage();

        //Create new Event
        AdminEventPage adminEventPage=new AdminEventPage(page);
        adminEventPage.createEvent(data.get("title"),
                data.get("description"),
                data.get("category"),
                data.get("city"),
                data.get("venue"),
                data.get("dateTime"),
                data.get("price"),
                data.get("totalSeats"));

        //Navigate to event page
        EventPage eventPage=new EventPage(page);
        eventPage.goTo();
        Locator myEventCard=eventPage.findMyEventCard(data.get("title"));
        int seatCountBefore=eventPage.getSeatCount(myEventCard);
        eventPage.proceedToBookEvent(myEventCard);


        //Navigate to Book event
        BookEventPage bookeventPage=new BookEventPage(page);
        bookeventPage.validateBookEventPage();
        bookeventPage.proceedToBookEventAndConfirm(data.get("fullName"),
                data.get("email"),
                data.get("phone"));
        String bookingRef= bookeventPage.getBookingRef();

        //Validate my booking page by verifying the booking ref
        MyBookingsPage myBookingspage=new MyBookingsPage(page);
        myBookingspage.validateMyBookings(bookingRef);

        //Validate the seat count as decreased
        String seatCountAfterBooking=eventPage.getSeatCountAfterBooking(data.get("title"));

        //Compare the seat count before and after booking
        eventPage.compareSeatCount(seatCountAfterBooking,seatCountBefore);

    }


}
