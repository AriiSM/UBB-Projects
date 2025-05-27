package org.example.steps.serenity;

import net.thucydides.core.annotations.Step;
import net.thucydides.core.steps.ScenarioSteps;
import org.example.pages.DictionaryPage;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

public class EndUserSteps extends ScenarioSteps {

    DictionaryPage dictionaryPage;

    @Step
    public void is_the_login_page() {
        dictionaryPage.open_login_page();
    }

    @Step
    public void enter_login_credentials(String username, String password) {
        dictionaryPage.type_username(username);
        dictionaryPage.type_password(password);
    }

    @Step
    public void submit_login() {
        dictionaryPage.click_login_button();
    }

    @Step
    public void should_see_logged_in_user(String username) {
        dictionaryPage.verify_user_logged_in(username);
    }

    @Step
    public void should_see_login_error() {
        dictionaryPage.verify_login_error_displayed();
    }

    @Step
    public void goes_to_login_page() {
        dictionaryPage.goToLoginPage();
    }

    @Step
    public void logs_in_as(String username, String password) {
        dictionaryPage.loginAs(username, password);
    }

    @Step
    public void looks_for(String term) {
        dictionaryPage.enter_keywords(term);
        dictionaryPage.lookup_terms();
    }

    @Step
    public void adds_current_page_to_watchlist() {
        dictionaryPage.addCurrentPageToWatchlist();
    }

    @Step
    public void opens_own_profile() {
        dictionaryPage.openProfilePage();
    }

    @Step
    public void tries_exact_match() {
        dictionaryPage.clickExactMatch();
    }

    @Step
    public void should_see_logged_user(String username) {
        assertThat(dictionaryPage.getPageSource(), containsString(username));
    }

    @Step
    public void should_see_page_title_contains(String word) {
        assertThat(dictionaryPage.getPageTitle(), containsString(word));
    }

    @Step
    public void should_see_page_added_to_watchlist() {
        assertTrue(dictionaryPage.isPageWatched(), "Pagina nu a fost adaugata la Watchlist!");
    }

    private void assertTrue(boolean pageWatched, String s) {
        if (!pageWatched) {
            throw new AssertionError(s);
        }
    }
}