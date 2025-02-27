import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.User;
import pojo.UserLogin;

import static org.apache.http.HttpStatus.*;

public class CreateUserTest {

    User user = UserGenerator.getRandomUser();
    UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
    UserClient client = new UserClient();
    String token;

    @After
    public void cleanUp() {
        Response response = client.loginUser(login);
        token = client.getToken(response);
        client.deleteUser(token);
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка ручки создание пользователя POST api/auth/register")
    public void createUniqueUserTest() {
        Response response = client.createUser(user);
        client.compareStatusCode(response, SC_OK);
        client.compareResponseBody(response);
    }

    @Test
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверка ручки создание пользователя POST api/auth/register")
    public void createDoubleUserTest() {
        client.createUser(user);
        Response response = client.createUser(user);
        client.compareStatusCode(response, SC_FORBIDDEN);
        client.compareResponseBodyMessage(response, "User already exists");
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка ручки создание пользователя POST api/auth/register")
    public void createUserWithoutEmailTest() {
        client.createUser(user);
        User userWithoutEmail = new User("", "password", "Ivan");
        Response response = client.createUser(userWithoutEmail);
        client.compareStatusCode(response, SC_FORBIDDEN);
        client.compareResponseBodyMessage(response, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без password")
    @Description("Проверка ручки создание пользователя POST api/auth/register")
    public void createUserWithoutPasswordTest() {
        client.createUser(user);
        User userWithoutPassword = new User("user-test@ya.ru", "", "Ivan");
        Response response = client.createUser(userWithoutPassword);
        client.compareStatusCode(response, SC_FORBIDDEN);
        client.compareResponseBodyMessage(response, "Email, password and name are required fields");
    }

    @Test
    @DisplayName("Создание пользователя без name")
    @Description("Проверка ручки создание пользователя POST api/auth/register")
    public void createUserWithoutNameTest() {
        client.createUser(user);
        User userWithoutName = new User("user-test@ya.ru", "password", "");
        Response response = client.createUser(userWithoutName);
        client.compareStatusCode(response, SC_FORBIDDEN);
        client.compareResponseBodyMessage(response, "Email, password and name are required fields");
    }
}
