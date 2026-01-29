package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginInput = $("[data-test-id=login] input");
    private final SelenideElement passwordInput = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage login(String login, String password) {
        submitLogin(login, password);
        return new VerificationPage();
    }

    public void loginExpectError(String login, String password) {
        submitLogin(login, password);
        shouldSeeErrorNotificationWithText("Ошибка Ошибка! Неверно указан логин или пароль");
    }

    public void loginExpectBlocked(String login, String password) {
        submitLogin(login, password);
        shouldSeeErrorNotificationWithText("Пользователь заблокирован");
    }

    public void shouldSeeErrorNotificationWithText(String expectedText) {
        errorNotification.shouldBe(visible).shouldHave(text(expectedText));
    }

    private void submitLogin(String login, String password) {
        loginInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        clearFields();
        loginInput.setValue(login);
        passwordInput.setValue(password);
        loginButton.click();
    }

    private void clearFields() {
        loginInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        passwordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
    }
}
