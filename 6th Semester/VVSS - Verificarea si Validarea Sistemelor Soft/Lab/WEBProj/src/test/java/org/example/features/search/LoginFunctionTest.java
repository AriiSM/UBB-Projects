package org.example.features.search;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.Qualifier;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.example.steps.serenity.EndUserSteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/login_data.csv")
public class LoginFunctionTest {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public EndUserSteps endUser;

    // Variables for CSV data
    private String username;
    private String password;
    private String expectedResult;

    @Qualifier
    public String qualifier() {
        return username + " - " + expectedResult;
    }

    // Getters and setters for CSV data
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    @Test
    public void loginTest() {
        // Open login page
        endUser.is_the_login_page();

        // Enter credentials
        endUser.enter_login_credentials(username, password);

        // Click login button
        endUser.submit_login();

        // Check results based on expected data
        if (expectedResult.equals("success")) {
            // Test with valid data
            endUser.should_see_logged_in_user(username);
        } else {
            // Test with invalid data
            endUser.should_see_login_error();
        }
    }
}