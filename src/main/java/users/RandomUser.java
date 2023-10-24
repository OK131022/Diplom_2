package users;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUser {

    public static User getRandomUser() {
        String email = RandomStringUtils.randomAlphabetic(5) + "@ya.ru";
        String password = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        return new User(email, password, name);
    }

    public static User getUserWithoutEmail() {
        String email = "";
        String password = RandomStringUtils.randomAlphanumeric(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        return new User(email, password, name);
    }

    public static User getUserWithoutPassword() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@ya.ru";
        String password = "";
        String name = RandomStringUtils.randomAlphabetic(5);
        return new User(email, password, name);
    }

    public static User getUserWithoutName() {
        String email = RandomStringUtils.randomAlphanumeric(5) + "@ya.ru";
        String password = RandomStringUtils.randomAlphanumeric(5);
        String name = "";
        return new User(email, password, name);
    }
}