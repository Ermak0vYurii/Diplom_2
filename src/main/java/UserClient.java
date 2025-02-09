import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserClient extends BaseHttpClient {

    @Step("Регистрация пользователя. Send POST request to api/auth/register")
    public Response createUser(Object user) {
        return doPostRequest(Endpoints.PATH_REGISTER, user);
    }

    @Step("Авторизация пользователя. Send POST request to api/auth/login")
    public Response loginUser(Object userLogin) {
        return doPostRequest(Endpoints.PATH_LOGIN, userLogin);
    }

    @Step("Get access token")
    public String getToken(Response response) {
        return response.then().extract().body().path("accessToken");
    }

    @Step("Удаление пользователя. Send DELETE request to api/auth/user")
    public void deleteUser(String token) {
        doDeleteRequest(Endpoints.PATH_USER, token);
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
    public void compareResponseBodyMessage(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }

    @Step("Изменение авторизованного пользователя. Send PATCH request to api/auth/user")
    public Response editUser(Object body, String token) {
       return doPatchRequest(Endpoints.PATH_USER, body, token);
    }

    @Step("Изменение неавторизованного пользователя. Send PATCH request to api/auth/user")
    public Response editUser(Object body) {
        return doPatchRequest(Endpoints.PATH_USER, body);
    }

    @Step("Compare response body user new email")
    public void compareResponseBodyUserEmail(Response response, String newEmail) {
        response.then().assertThat().body("user.email", equalTo(newEmail));
    }

    @Step("Compare response body user new name")
    public void compareResponseBodyUserName(Response response, String newName) {
        response.then().assertThat().body("user.name", equalTo(newName));
    }

}
