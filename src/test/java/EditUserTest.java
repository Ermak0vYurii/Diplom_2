import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserLogin;

import static org.apache.http.HttpStatus.*;

public class EditUserTest {

    User user = new User("user-test@ya.ru", "password", "Ivan");
    UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
    UserClient client = new UserClient();
    String token;

    @Before
    public void setUp() {
        Response response = client.createUser(user);
        token = client.getToken(response);
    }

    @After
    public void cleanUp() {
        client.deleteUser(token);
    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    public void editEmailAuthUserTest() {
        client.loginUser(login);
        User editEmailUser = new User("updateemail@ya.ru", null, null);
        Response response = client.editUser(editEmailUser, token);
        client.compareStatusCode(response, SC_OK);
        client.compareResponseBodyUserEmail(response, "updateemail@ya.ru");
    }

    @Test
    @DisplayName("Изменение name авторизованного пользователя")
    public void editNameAuthUserTest() {
        client.loginUser(login);
        User editNameUser = new User(null, null, "Alex");
        Response response = client.editUser(editNameUser, token);
        client.compareStatusCode(response, SC_OK);
        client.compareResponseBodyUserName(response, "Alex");
    }

    @Test
    @DisplayName("Изменение password авторизованного пользователя")
    public void editPasswordAuthUserTest() {
        client.loginUser(login);
        User editPasswordUser = new User(null, "newpassword", null);
        Response response = client.editUser(editPasswordUser, token);
        client.compareStatusCode(response, SC_OK);
        client.compareResponseBody(response);
    }
}
