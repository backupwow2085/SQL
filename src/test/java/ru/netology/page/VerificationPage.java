package ru.netology.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {

    private final SelenideElement codeInput = $("[data-test-id=code] input");
    private final SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public void shouldBeOpened() {
        codeInput.shouldBe(visible);
        verifyButton.shouldBe(visible);
    }

    public DashboardPage verify(String code) {
        codeInput.shouldBe(visible).setValue(code);
        verifyButton.click();
        return new DashboardPage();
    }
}
