import io.qameta.allure.Step;
import io.restassured.response.Response;

import static com.sun.org.apache.xerces.internal.util.PropertyState.is;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserClient extends BaseHttpClient {

    private final static String PATH_REGISTER = "/api/auth/register";
    private final static String PATH_USER = "/api/auth/user";
    private final static String PATH_LOGIN = "/api/auth/login";

    @Step("Регистрация пользователя. Send POST request to api/auth/register")
    public Response createUser(Object user) {
        return doPostRequest(PATH_REGISTER, user);
    }

    @Step("Авторизация пользователя. Send POST request to api/auth/login")
    public Response loginUser(Object userLogin) {
        return doPostRequest(PATH_LOGIN, userLogin);
    }

    @Step("Get access token")
    public String getToken(Response response) {
        return response.then().extract().body().path("accessToken");
    }

    @Step("Удаление пользователя. Send DELETE request to api/auth/user")
    public Response deleteUser(String token) {
        return doDeleteRequest(PATH_USER, token);
    }

    @Step("Compare status code")
    public void compareStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Compare response body")
    public void compareResponseBody(Response response) {
        response.then().assertThat().body("user", notNullValue());
    }

    @Step("Compare body error message")
    public void compareResponseBodyMessage(Response response) {
        response.then().assertThat().body("message", equalTo("User already exists"));
    }

}
