package ru.netology.page;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement loginInput = $("[data-test-id=login] input");
    private final SelenideElement passwordInput = $("[data-test-id=password] input");
    private final SelenideElement loginButton = $("[data-test-id=action-login]");
    private final SelenideElement errorNotification = $("[data-test-id=error-notification]");

    public VerificationPage login(String login, String password) {
        loginInput.shouldBe(visible);
        loginInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        loginInput.setValue(login);
        passwordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        passwordInput.setValue(password);
        loginButton.click();
        return new VerificationPage();
    }

    public void loginExpectError(String login, String password) {
        loginInput.shouldBe(visible);
        loginInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        loginInput.setValue(login);
        passwordInput.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
        passwordInput.setValue(password);
        loginButton.click();
        errorNotification.shouldBe(visible);
    }

    public void shouldSeeErrorNotification() {
        errorNotification.shouldBe(visible);
    }
}
