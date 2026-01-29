package ru.netology.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import ru.netology.data.DataHelper;
import ru.netology.data.SQLHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

@TestMethodOrder(OrderAnnotation.class)
class LoginTest {

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @AfterAll
    static void tearDownAll() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @Order(1)
    void shouldLoginWithValidUser() {
        var user = DataHelper.getValidUser();

        var loginPage = new LoginPage();
        var verificationPage = loginPage.login(user.getLogin(), user.getPassword());

        verificationPage.shouldBeOpened();
        var code = SQLHelper.getAuthCode(user.getLogin());
        org.junit.jupiter.api.Assertions.assertNotNull(code, "Код не был сгенерирован");
        var dashboardPage = verificationPage.verify(code);

        dashboardPage.shouldBeOpened();
    }

    @Test
    @Order(2)
    void shouldBlockUserAfterThreeInvalidAttempts() {
        var userWithWrongPassword = DataHelper.getUserWithWrongPassword();
        var loginPage = new LoginPage();

        for (int i = 0; i < 3; i++) {
            loginPage.loginExpectError(userWithWrongPassword.getLogin(), userWithWrongPassword.getPassword());
        }

        var validUser = DataHelper.getValidUser();
        loginPage.loginExpectBlocked(validUser.getLogin(), validUser.getPassword());
    }
}
