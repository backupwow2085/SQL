package ru.netology.page;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.Selenide.webdriver;

public class DashboardPage {

    public DashboardPage() {
    }

    public void shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/dashboard"));
        $("[data-test-id=error-notification]").shouldNotBe(visible);
    }
}
