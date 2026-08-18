package org.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class HomePage {

    Page page;

    public HomePage(Page page) {
        this.page=page;
    }

    public void validateHomePage(){
        assertThat(page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("My Bookings"))).isVisible();
    }

}
