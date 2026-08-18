package org.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.example.Locators.BookEventPageLocators;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookEventPage extends BookEventPageLocators {

    Page page;

    public BookEventPage(Page page) {
        this.page=page;
    }
    public void validateBookEventPage(){
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Book Tickets"))).isVisible();
    }

    public void proceedToBookEventAndConfirm(String fullName,String email, String phoneNumber){
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+")).click();
        page.getByPlaceholder(fullNamePlaceholder).fill(fullName);
        page.getByLabel(emailLabel).fill(email);
        page.getByLabel(phoneNumberLabel).fill(phoneNumber);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Confirm Booking")).click();
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
    }

    public String getBookingRef(){
        String bookingRef=page.locator(bookingRefCss).innerText();
        System.out.println("Booking ref: "+bookingRef);
        return bookingRef;
    }
}
