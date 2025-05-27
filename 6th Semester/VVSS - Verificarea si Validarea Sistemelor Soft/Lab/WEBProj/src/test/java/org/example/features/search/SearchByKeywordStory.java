package org.example.features.search;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Issue;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Pending;
import net.thucydides.core.annotations.Steps;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import org.example.steps.serenity.EndUserSteps;

@RunWith(SerenityRunner.class)
public class SearchByKeywordStory {

    @Managed(uniqueSession = true)
    public WebDriver webdriver;

    @Steps
    public EndUserSteps anna;

    @Test
    public void user_can_login_search_and_logout() {
        anna.goes_to_login_page();
        anna.logs_in_as("TestDA223", "parolaincorecta");
        anna.should_see_logged_user("TestDA223");

        anna.looks_for("cat");
        anna.should_see_page_title_contains("cat");

        anna.adds_current_page_to_watchlist();
        anna.should_see_page_added_to_watchlist();

        anna.opens_own_profile();
        anna.should_see_page_title_contains("TestDA223");

        anna.tries_exact_match();
        anna.should_see_page_title_contains("User:TestDA223");
    }

}