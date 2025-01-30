import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.User;
import pojo.UserLogin;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest {

    User user = new User("user-test@ya.ru", "password", "Ivan");
    UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
    UserClient client = new UserClient();

    @After
    public void cleanUp() {
        Response response = client.loginUser(login);
        String token = client.getToken(response);
        client.deleteUser(token);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    public void createUniqueUserTest() {
        Response response = client.createUser(user);
        client.compareStatusCode(response, SC_OK);
        client.compareResponseBody(response);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    public void createDoubleUserTest() {
        client.createUser(user);
        Response response = client.createUser(user);
        client.compareStatusCode(response, SC_FORBIDDEN);
        client.compareResponseBodyMessage(response);
    }
}
