import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PostTests {
    public static Logger logger = LogManager.getLogger(PostTests.class);

    String root = "https://jsonplaceholder.typicode.com";

    public final class EndPoints {
        public static String post = "/posts";

        public static String requestBody = "{\n" +
                "  \"title\": \"My title\",\n" +
                "  \"body\": \"My body\",\n" +
                "  \"userId\": \"1111\" \n}";
    }

    @Test
    public void responsePostNonFluentPassTest() {
        logger.info("Post, non fluent, pass test");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(EndPoints.requestBody);
        request.then().statusCode(201);
        Response response = request.post(root + EndPoints.post);
        //response.prettyPrint();
        //проверки
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        String body = response.path("body");
        if (body.contains("My body"))
            logger.info("body = My body");
        else
            logger.info("body != My body");
        logger.info("Test over");
    }

    @Test
    public void responsePostNonFluentFailTest() {
        logger.info("Post, non fluent, fail test");
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.body(EndPoints.requestBody);
        request.then().statusCode(502);
        //request.then().statusCode(201);
        Response response = request.post(root + EndPoints.post);
        //response.prettyPrint();
        //проверки
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        String UserId = response.path("userId");
        if (UserId.contains("1111"))
            logger.info("UserId = 1111");
        else
            logger.info("UserId != 1111");
        logger.info("Test over");
    }

    @Test
    public void responsePostFluentPassTest() {
        logger.info("Post, fluent, pass test");
        Response response = RestAssured
                .given()
                .baseUri(root)
                .header("Content-Type", "application/json")
                .body(EndPoints.requestBody)
                .expect()
                .statusCode(201)
                .when()
                .post(EndPoints.post);
        //response.prettyPrint();
        //проверки
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        String title = response.path("title");
        if (title.contains("My title"))
            logger.info("UserId = My title");
        else
            logger.info("UserId != My title");
        logger.info("Test over");
    }

    @Test
    public void responsePostFluentFailTest() {
        logger.info("Post, fluent, fail test");
        Response response = RestAssured
                .given()
                .baseUri(root)
                .header("Content-Type", "application/json")
                .body(EndPoints.requestBody)
                .expect()
                .statusCode(502)
                //.statusCode(201)
                .when()
                .post(EndPoints.post);
        //response.prettyPrint();
        //проверки
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        Integer userId = response.path("id");
        if (userId.equals(101))
            logger.info("Id = 101");
        else
            logger.info("Id != 101");
        logger.info("Test over");
    }
}
