import clients.ClientUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.RandomUser;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateUserTest {

    private User user;
    private ClientUser userClient;
    private String userToken;

    @Before
    @Description("Создание уникального пользователя")
    public void setUp() {
        user = RandomUser.getRandomUser();
        userClient = new ClientUser();
    }


    @Test
    @DisplayName("Создание пользователя с корректными данными, код 200")
    public void createUserSuccess() {
        userToken = userClient.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .extract().path("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрировался, ошибка 403")
    public void createDuplicateUser() {

        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));

        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email, ошибка 403")
    public void createUserWithoutEmail() {
        user = RandomUser.getUserWithoutEmail();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без password, ошибка 403")
    public void createUserWithoutPassword() {
        user = RandomUser.getUserWithoutPassword();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени, ошибка 403")
    public void createUserWithoutName() {
        user = RandomUser.getUserWithoutName();
        userClient.createUser(user)
                .assertThat()
                .statusCode(SC_FORBIDDEN)
                .and()
                .body("success", is(false))
                .and()
                .assertThat().body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }
}
