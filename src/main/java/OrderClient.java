import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderClient extends BaseHttpClient {

    private final static String PATH_ORDER = "/api/orders";
    private final static String PATH_INGREDIENTS = "/api/ingredients";

    @Step("Send GET request to api/ingredients")
    public Response getIngredients() {
        return doGetRequest(PATH_INGREDIENTS);
    }

    @Step("Get ingredients _id")
    public List<String> getIngredientsId(Response response) {
        return response.then().extract().jsonPath().getList("data._id");
    }

    @Step("Send POST request to api/orders")
    public Response createOrder(Object body) {
        return doPostRequest(PATH_ORDER, body);
    }

    @Step("Send POST request to api/orders")
    public Response createOrderAuthUser(Object body, String token) {
        return doPostRequest(PATH_ORDER, body, token);
    }

    @Step("Compare status code")
    public void compareStatusCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Compare response body")
    public void compareResponseBody(Response response) {
        response.then().assertThat().body("success", equalTo(true))
                .and().assertThat().body("order.number", notNullValue());
    }

    @Step("Compare response body")
    public void compareResponseBodyAuthUser(Response response) {
        response.then().assertThat().body("success", equalTo(true))
                .and().assertThat().body("order.owner", notNullValue());
    }

    @Step("Compare response error message")
    public void compareErrorMessage(Response response, String message) {
        response.then().assertThat().body("message", equalTo(message));
    }

    @Step("Send GET request to api/orders")
    public Response getOrdersAuthUser(String token) {
        return doGetRequest(PATH_ORDER, token);
    }

    @Step("Send GET request to api/orders")
    public Response getOrdersWithoutAuth() {
        return doGetRequest(PATH_ORDER);
    }

    @Step("Compare response body")
    public void compareResponseBodyGetOrders(Response response) {
        response.then().assertThat().body("success", equalTo(true))
                .and().assertThat().body("orders", notNullValue());
    }
}
