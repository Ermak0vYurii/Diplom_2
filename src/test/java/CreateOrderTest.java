import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Order;
import pojo.User;
import pojo.UserLogin;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class CreateOrderTest {

    User user = new User("user-test@ya.ru", "password", "Ivan");
    UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    String token;

    @Before
    public void setUp() {
        Response response = userClient.createUser(user);
        token = userClient.getToken(response);
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами и авторизацией")
    public void createOrderWithAuthTest() {
        userClient.loginUser(login);
        Response responseIngredients = orderClient.getIngredients();
        List<String> idIngredients = orderClient.getIngredientsId(responseIngredients);
        List<String> ingredientsForOrder = new ArrayList<>();
            ingredientsForOrder.add(idIngredients.get(0));
            ingredientsForOrder.add(idIngredients.get(1));
        Order order = new Order(ingredientsForOrder);
        Response response = orderClient.createOrderAuthUser(order, token);
        orderClient.compareStatusCode(response, SC_OK);
        orderClient.compareResponseBodyAuthUser(response);
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    public void createOrderWithoutAuthTest() {
        Response responseIngredients = orderClient.getIngredients();
        List<String> idIngredients = orderClient.getIngredientsId(responseIngredients);
        List<String> ingredientsForOrder = new ArrayList<>();
            ingredientsForOrder.add(idIngredients.get(0));
            ingredientsForOrder.add(idIngredients.get(1));
        Order order = new Order(ingredientsForOrder);
        Response response = orderClient.createOrder(order);
        orderClient.compareStatusCode(response, SC_OK);
        orderClient.compareResponseBody(response);
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredientsTest() {
        List<String> ingredientsForOrder = new ArrayList<>();
        Order order = new Order(ingredientsForOrder);
        Response response = orderClient.createOrder(order);
        orderClient.compareStatusCode(response, SC_BAD_REQUEST);
        orderClient.compareErrorMessage(response, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredientsTest() {
        List<String> invalidIngredients = new ArrayList<>();
            invalidIngredients.add("1c0c5a71d1f82001bdaaa6d");
            invalidIngredients.add("1c0c5a71d1f82001bdaaa6f");
        Order order = new Order(invalidIngredients);
        Response response = orderClient.createOrder(order);
        orderClient.compareStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }

}
