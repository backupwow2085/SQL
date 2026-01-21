package ru.netology.data;

import com.github.javafaker.Faker;

public class DataHelper {

    private static final Faker faker = new Faker();

    private DataHelper() {
    }

    public static AuthInfo getValidUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static AuthInfo getInvalidUser() {
        return new AuthInfo(
                faker.name().username(),
                faker.internet().password()
        );
    }

    public static AuthInfo getUserWithWrongPassword() {
        return new AuthInfo(
                "vasya",
                faker.internet().password()
        );
    }

    public static class AuthInfo {
        private final String login;
        private final String password;

        public AuthInfo(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }
}
