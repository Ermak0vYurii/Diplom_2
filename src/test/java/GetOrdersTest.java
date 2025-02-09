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

public class GetOrdersTest {

    User user = UserGenerator.getRandomUser();
    UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
    UserClient userClient = new UserClient();
    OrderClient orderClient = new OrderClient();
    String token;
    List<String> ingredientsForOrder;
    Order order;

    @Before
    public void setUp() {
        Response response = userClient.createUser(user);
        token = userClient.getToken(response);
        Response responseIngredients = orderClient.getIngredients();
        List<String> idIngredients = orderClient.getIngredientsId(responseIngredients);
        ingredientsForOrder = new ArrayList<>();
        ingredientsForOrder.add(idIngredients.get(0));
        ingredientsForOrder.add(idIngredients.get(1));
        order = new Order(ingredientsForOrder);
    }

    @After
    public void cleanUp() {
        userClient.deleteUser(token);
    }

    @Test
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("Проверка ручки получения списка заказов пользователя GET api/orders")
    public void getOrdersAuthUserTest() {
        userClient.loginUser(login);
        orderClient.createOrderAuthUser(order, token);
        Response response = orderClient.getOrdersAuthUser(token);
        orderClient.compareStatusCode(response, SC_OK);
        orderClient.compareResponseBodyGetOrders(response);
    }

    @Test
    @DisplayName("Получение списка заказов неавторизованного пользователя")
    @Description("Проверка ручки получения списка заказов пользователя GET api/orders")
    public void getOrdersWithoutAuthTest() {
        orderClient.createOrder(order);
        Response response = orderClient.getOrdersWithoutAuth();
        orderClient.compareStatusCode(response, SC_UNAUTHORIZED);
        orderClient.compareErrorMessage(response, "You should be authorised");
    }
}