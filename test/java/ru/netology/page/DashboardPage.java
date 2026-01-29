package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;
import static com.codeborne.selenide.Selenide.webdriver;

public class DashboardPage {

    private final SelenideElement heading = $("h2.heading");

    public DashboardPage() {
    }

    public void shouldBeOpened() {
        webdriver().shouldHave(urlContaining("/dashboard"));
        $("[data-test-id=error-notification]").shouldNotBe(visible);
        heading.shouldBe(visible).shouldHave(text("Личный кабинет"));
    }
}
