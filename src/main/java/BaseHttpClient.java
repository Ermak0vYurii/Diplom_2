import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.ErrorLoggingFilter;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public abstract class BaseHttpClient  {

    public static final String URL = "https://stellarburgers.nomoreparties.site/";

    private final RequestSpecification baseRequestSpec = new RequestSpecBuilder()
            .setBaseUri(URL)
            .addHeader("Content-type", "application/json")
            .setRelaxedHTTPSValidation()
            .addFilter(new RequestLoggingFilter())
            .addFilter(new ResponseLoggingFilter())
            .addFilter(new ErrorLoggingFilter())
            .build();

    protected Response doPostRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .post(path)
                .thenReturn();
    }

    protected Response doPostRequest(String path, Object body, String token) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .body(body)
                .post(path)
                .thenReturn();
    }

    protected void doDeleteRequest(String path, String token) {
        given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .delete(path)
                .thenReturn();
    }

    protected Response doPatchRequest(String path, Object body, String token) {
        return given()
                .spec(baseRequestSpec)
                .header("Authorization", token)
                .body(body)
                .patch(path)
                .thenReturn();
    }

    protected Response doPatchRequest(String path, Object body) {
        return given()
                .spec(baseRequestSpec)
                .body(body)
                .patch(path)
                .thenReturn();
    }

    protected Response doGetRequest(String path) {
        return given()
                .spec(baseRequestSpec)
                .get(path)
                .thenReturn();
    }
}
