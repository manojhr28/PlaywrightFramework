package org.example.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.example.Locators.EventPageLocators;
import org.testng.Assert;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventPage extends EventPageLocators {

    Page page;

    public EventPage(Page page) {
        this.page = page;
    }

    public void goTo(){
        page.locator(navigateToEventTabId).click();
    }

    public Locator WaitEventCardsToLoad() {

        Locator eventCard = page.getByTestId(eventCardTestId);
        eventCard.first().isVisible();
        return eventCard;
    }

    public Locator findMyEventCard(String title){
        Locator eventCard=WaitEventCardsToLoad();
        Locator myEventCard=eventCard.filter(new Locator.FilterOptions().setHasText(title));
        assertThat(myEventCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000)); //if specific locator takes time then use this option for specific locator to wait
        return myEventCard;
    }

    public int getSeatCount(Locator myEventCard){
        String seatCountBeforeBooking = myEventCard.getByText(seatsAvailable).innerText();
        int seatCountBefore=Integer.parseInt(seatCountBeforeBooking.split(" ")[0]);
        System.out.println("Seat count before booking: "+seatCountBefore);
        return seatCountBefore;
    }

    public void proceedToBookEvent(Locator myEventCard){
        myEventCard.getByTestId("book-now-btn").click();
    }

    public String getSeatCountAfterBooking(String title){
        page.locator(navigateToEventTabId).click();
        page.waitForTimeout(1000);
        Locator eventCardAfterBooking=page.getByTestId(eventCardTestId);
        Locator myEventCardAfterBooking=eventCardAfterBooking.filter(new Locator.FilterOptions().setHasText(title));
        return myEventCardAfterBooking.getByText(seatsAvailable).innerText();
    }

    public void compareSeatCount(String seatCountAfterBooking, int seatCountBefore){
        int seatCountAfter=Integer.parseInt(seatCountAfterBooking.split(" ")[0]);
        System.out.println("Seat count after booking: "+seatCountAfter);
        Assert.assertTrue(seatCountBefore>seatCountAfter);
    }
}
