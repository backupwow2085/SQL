package ru.netology.test;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import java.util.Map;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.WebDriverRunner.url;
import static org.junit.jupiter.api.Assertions.fail;

class LoginTest {

    @BeforeAll
    static void setupBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false
        ));
        Configuration.browserCapabilities = options;
    }

    @BeforeEach
    void setUp() {
        SQLHelper.cleanDatabase();
        var user = DataHelper.getValidUser();
        SQLHelper.createUser(user.getLogin(), user.getPassword());
        SQLHelper.resetUserStatus(user.getLogin());
        open("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        SQLHelper.cleanDatabase();
        closeWebDriver();
    }

    @Test
    void shouldLoginWithValidUser() {
        var user = DataHelper.getValidUser();

        var loginPage = new LoginPage();
        var verificationPage = loginPage.login(user.getLogin(), user.getPassword());

        var code = SQLHelper.getAuthCode(user.getLogin());
        org.junit.jupiter.api.Assertions.assertNotNull(code, "Auth code was not generated");
        var dashboardPage = verificationPage.verify(code);

        dashboardPage.shouldBeOpened();
    }

    @Test
    void shouldBlockUserAfterThreeInvalidAttempts() {
        var userWithWrongPassword = DataHelper.getUserWithWrongPassword();
        var loginPage = new LoginPage();

        for (int i = 0; i < 3; i++) {
            loginPage.loginExpectError(userWithWrongPassword.getLogin(), userWithWrongPassword.getPassword());
        }

        var validUser = DataHelper.getValidUser();
        loginPage.login(validUser.getLogin(), validUser.getPassword());
        sleep(500);
        if (url().contains("verification")) {
            fail("Баг: после трёх неверных попыток открывается ввод кода подтверждения.");
        }
    }
}
