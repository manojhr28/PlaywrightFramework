package org.example.pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import org.example.Locators.AdminEventPageLocators;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class AdminEventPage extends AdminEventPageLocators {

    Page page;

    public AdminEventPage(Page page) {
        this.page=page;
    }

    public void createEvent(String title, String description, String category, String city, String venue, String eventDateTime, String price, String seats)
            throws InterruptedException {
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Manage Events")).nth(1).click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("+ New Event"))).isVisible();

        page.getByPlaceholder(eventTitlePlaceholder).fill(title);
        page.getByPlaceholder(eventDescriptionPlaceholder).fill(description);
        page.getByLabel(categoryLabel).selectOption(category);
        page.locator(cityId).fill(city);
        page.getByLabel(venueLabel).fill(venue);
        page.getByLabel(eventDateTimeLabel).fill(eventDateTime);
        page.getByLabel(priceLabel).fill(price);
        page.getByLabel(seatsLabel).fill(seats);
        Thread.sleep(3000);
        page.locator(createEventButtonId).click();
        assertThat(page.getByText("Event created!")).isVisible();
    }
}
