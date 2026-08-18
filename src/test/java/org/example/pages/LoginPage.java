package org.example.pages;

import org.example.Locators.LoginPageLocators;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class LoginPage extends LoginPageLocators {

    Page page;
    String baseUrl;



    public LoginPage(Page page,String baseUrl) {
        this.page=page;
        this.baseUrl=baseUrl;
    }
    public void login(String  username,String password){
        //https://eventhub.rahulshettyacademy.com/login
        //unknown1@gmail.com & unknown@1

        page.navigate(baseUrl);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder(email_placeholder).fill(username);
        page.getByLabel(password_label).fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
    }

}
