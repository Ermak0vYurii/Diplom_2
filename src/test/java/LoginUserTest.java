import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.User;
import pojo.UserLogin;

import static org.apache.http.HttpStatus.*;

public class LoginUserTest {

    User user = UserGenerator.getRandomUser();
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
    @DisplayName("Логин под существующим пользователем")
    @Description("Проверка ручки авторизация пользователя POST api/auth/login")
    public void loginUserTest() {
        Response response = client.loginUser(login);
        client.compareStatusCode(response, SC_OK);
        client.compareResponseBody(response);
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверка ручки авторизация пользователя POST api/auth/login")
    public void loginWithInvalidLogin() {
        User userWithInvalidLogin = new User("test@mail.ru", user.getPassword(), user.getName());
        UserLogin loginWithInvalidLogin = new UserLogin(userWithInvalidLogin.getEmail(), user.getPassword());
        Response response = client.loginUser(loginWithInvalidLogin);
        client.compareStatusCode(response, SC_UNAUTHORIZED);
        client.compareResponseBodyMessage(response, "email or password are incorrect");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверка ручки авторизация пользователя POST api/auth/login")
    public void loginWithInvalidPassword() {
        User userWithInvalidPassword = new User(user.getEmail(), "incorrect", user.getName());
        UserLogin loginWithInvalidPassword = new UserLogin(user.getEmail(), userWithInvalidPassword.getPassword());
        Response response = client.loginUser(loginWithInvalidPassword);
        client.compareStatusCode(response, SC_UNAUTHORIZED);
        client.compareResponseBodyMessage(response, "email or password are incorrect");
    }
}