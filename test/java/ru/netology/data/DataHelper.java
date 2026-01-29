package ru.netology.data;

import com.github.javafaker.Faker;
import lombok.Value;

public class DataHelper {

    private static final Faker faker = new Faker();

    private DataHelper() {
    }

    public static AuthInfo getValidUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getUserWithWrongPassword() {
        return new AuthInfo(
                "vasya",
                faker.internet().password()
        );
    }

    @Value
    public static class AuthInfo {
        String login;
        String password;
    }
}
