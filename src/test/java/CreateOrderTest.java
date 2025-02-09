import io.qameta.allure.Description;
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

    User user = UserGenerator.getRandomUser();
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
    @Description("Проверка ручки создания заказа POST api/orders " +
            "c токеном в заголовке и ингредиентами в теле запроса ")
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
    @Description("Проверка ручки создания заказа POST api/orders c ингредиентами в теле запроса, без передачи токена")
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
    @Description("Проверка ручки создание заказа POST api/orders с пустым телом запроса")
    public void createOrderWithoutIngredientsTest() {
        List<String> ingredientsForOrder = new ArrayList<>();
        Order order = new Order(ingredientsForOrder);
        Response response = orderClient.createOrder(order);
        orderClient.compareStatusCode(response, SC_BAD_REQUEST);
        orderClient.compareErrorMessage(response, "Ingredient ids must be provided");
    }

    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверка ручки создание заказа POST api/orders с передачей в теле запроса неверный хеш ингредиентов")
    public void createOrderWithInvalidIngredientsTest() {
        List<String> invalidIngredients = new ArrayList<>();
            invalidIngredients.add("1c0c5a71d1f82001bdaaa6d");
            invalidIngredients.add("1c0c5a71d1f82001bdaaa6f");
        Order order = new Order(invalidIngredients);
        Response response = orderClient.createOrder(order);
        orderClient.compareStatusCode(response, SC_INTERNAL_SERVER_ERROR);
    }
}