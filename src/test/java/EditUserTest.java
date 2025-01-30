import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import pojo.User;
import pojo.UserLogin;

import static org.apache.http.HttpStatus.*;

public class EditUserTest {

    User user = new User("user-test@ya.ru", "password", "Ivan");
    UserLogin login = new UserLogin(user.getEmail(), user.getPassword());
    UserClient client = new UserClient();

//    @After
//    public void cleanUp() {
//        Response response = client.loginUser(login);
//        String token = client.getToken(response);
//        client.deleteUser(token);
//    }

    @Test
    @DisplayName("Изменение email авторизованного пользователя")
    public void editEmailAuthUserTest() {
        client.createUser(user);
        Response response = client.loginUser(login);
        String token = client.getToken(response);
        User editEmailUser = new User("updateemail@ya.ru", null, null);
        Response responseForTest = client.editUser(editEmailUser, token);
        client.compareStatusCode(responseForTest, SC_OK);
        client.compareResponseBodyUserData(responseForTest, "updateemail@ya.ru");
        client.deleteUser(token);
    }

    @Test
    @DisplayName("Изменение name авторизованного пользователя")
    public void editNameAuthUserTest() {
        client.createUser(user);
        Response response = client.loginUser(login);
        String token = client.getToken(response);
        User editNameUser = new User(null, null, "Alex");
        Response responseForTest = client.editUser(editNameUser, token);
        client.compareStatusCode(responseForTest, SC_OK);
        //здесь нужно написать проверку responseBody
        client.deleteUser(token);
    }

    @Test
    @DisplayName("Изменение password авторизованного пользователя")
    public void editPasswordAuthUserTest() {
        client.createUser(user);
        Response response = client.loginUser(login);
        String token = client.getToken(response);
        User editPasswordUser = new User(null, "newpassword", null);
        Response responseForTest = client.editUser(editPasswordUser, token);
        client.compareStatusCode(responseForTest, SC_OK);
        //здесь нужно написать проверку responseBody
        client.deleteUser(token);
    }
}
