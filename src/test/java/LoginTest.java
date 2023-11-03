import clients.ClientUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserLoginData;
import users.RandomUser;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class LoginTest {

    private User user;
    private ClientUser userClient;
    private UserLoginData userAuthData;
    private String userToken;

    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = RandomUser.getRandomUser();
        userClient = new ClientUser();
        userToken = userClient.createUser(user).extract().path("accessToken");
    }



    @Test
    @DisplayName("Логин под существующим пользователем, код 200")
    public void successLoginUser() {
        userClient.authUser(UserLoginData.withCorrectData(user))
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true));
    }

    @Test
    @DisplayName("Логин с неверной почтой, код 401")
    public void loginWithErrorEmail() {
        userAuthData = UserLoginData.withErrorEmail(user);
        userClient.authUser(userAuthData)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Логин с неверным паролем, код 401")
    public void loginWithErrorPassword() {
        userAuthData = UserLoginData.withErrorPassword(user);
        userClient.authUser(userAuthData)
                .assertThat()
                .statusCode(SC_UNAUTHORIZED)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("email or password are incorrect"));
    }

    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }
}