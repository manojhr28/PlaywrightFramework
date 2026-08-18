package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MyBookingsPage {

    Page page;

    public MyBookingsPage(Page page){
        this.page=page;
    }

    public void validateMyBookings(String bookingRef){

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("View My Bookings")).click();
        Locator bookingCards=page.getByTestId("booking-card");
        Locator myBooking=bookingCards.filter(new Locator.FilterOptions().setHasText(bookingRef));
        assertThat(myBooking).isVisible();
    }
}
