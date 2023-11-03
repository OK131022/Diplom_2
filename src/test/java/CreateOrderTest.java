import clients.ClientOrder;
import clients.ClientUser;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import orders.Order;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import users.User;
import users.UserLoginData;
import users.RandomUser;

import static org.hamcrest.CoreMatchers.*;
import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    private User user;
    private Order order;
    private ClientUser userClient;
    private ClientOrder orderClient;
    private String userToken;


    @Before
    @Description("Создание пользователя")
    public void setUp() {
        user = RandomUser.getRandomUser();
        order = new Order();
        userClient = new ClientUser();
        orderClient = new ClientOrder();
    }


    @Test
    @DisplayName("Создание заказа с ингридиентами авторизованным пользователем, код 200")
    public void createSuccessOrderWithAutorized() {

        order = Order.createIngredients();
        userToken = userClient.createUser(user).extract().path("accessToken");
        userClient.authUser(UserLoginData.withCorrectData(user));
        orderClient.createOrderWithAutorized(order, userToken)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("order.number", notNullValue())
                .and()
                .body("order._id", notNullValue());
    }


    @Test
    @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAutorized() {
        order = Order.createIngredients();
        orderClient.createOrderWithoutAutorized(order)
                .assertThat()
                .statusCode(SC_OK)
                .and()
                .body("success", is(true))
                .and()
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов, код 400")
    public void createOrderWithoutIngredients() {
        orderClient.createOrderWithoutAutorized(order)
                .assertThat()
                .statusCode(SC_BAD_REQUEST)
                .and()
                .body("success", is(false))
                .and()
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов, код 500")
    public void orderCreateWithInvalidHashIngredients() {
        order = Order.createIngredientsWithErrorHash();
        orderClient.createOrderWithoutAutorized(order)
                .assertThat()
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }


    @After
    @Description("Удаление пользователя")
    public void deleteUser() {
        if (userToken != null) {
            userClient.deleteUser(userToken);
        }
    }
}
