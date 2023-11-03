import clients.ClientUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserLoginData;
import users.RandomUser;

import java.util.Locale;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class EditUserTest {

    private User user;
    private ClientUser userClient;
    private String userToken;
    private User newUser;

    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = RandomUser.getRandomUser();
        userClient = new ClientUser();
        userToken = userClient.createUser(user).extract().path("accessToken");
        newUser = RandomUser.getRandomUser();
    }


    @Test
    @DisplayName("Успешное зменение данных пользователя с авторизацией")
    public void editUserWithAutorized() {
        userClient.authUser(UserLoginData.withCorrectData(user));
        userClient.editUser(newUser, userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("user.email", equalTo(newUser.getEmail().toLowerCase(Locale.ROOT)))
                .and()
                .body("user.name", equalTo(newUser.getName()));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации, код 401")
    public void editUserWithoutAutorized() {
        userClient.editUserWithoutAuth(newUser)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", is(false));
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }
}