import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsIterableContaining.hasItem;
import static org.hamcrest.core.StringContains.containsString;

public class GetTests {
    public static Logger logger = LogManager.getLogger(GetTests.class);
    public final String root = "https://api.opendota.com/api/";
    public static String leagueName = "BetBoom Xmas Show";

    public final class EndPoints {
        public static String proMatches = "/proMatches";
        public static String proTeams = "/teams";
    }

    @Test
    public void responseGetPassNonFluentTest() {
        logger.info("Get, non fluent, pass test");
        //искомая команда
        String teamSearch = "Unique";
        RequestSpecification request = RestAssured.given();
        request.baseUri(root);
        request.contentType(ContentType.JSON);
        request.then().statusCode(200);
        request.then().contentType("application/json");
        request.then().body("league_name", hasItem(leagueName));
        //за силы света команда не играла на турнирах, поэтому должен быть фейл
        //request.then().body("radiant_name",hasItem(teamSearch));
        request.then().body("dire_name", hasItem(teamSearch));
        request.log().method();
        request.log().headers();
        request.log().uri();
        Response response = request.get(EndPoints.proMatches);
        //response.prettyPrint();
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        //response.prettyPrint();
        List<String> listRadiant = response.path("findAll{it.league_name == '" + leagueName + "'}.radiant_name");
        List<String> listDire = response.path("findAll{it.league_name == '" + leagueName + "'}.dire_name");
        if (listRadiant.contains(teamSearch) || listDire.contains(teamSearch))
            logger.info("Команда " + teamSearch + " найдена в лиге " + leagueName);
        else
            logger.info("Команда " + teamSearch + " не найдена в лиге " + leagueName);
        logger.info("Test over");
    }

    @Test
    public void responseGetPassFluentTest() {
        logger.info("Get, fluent, pass test");
        //искомая команда
        String teamSearch = "Unique";
        Response response = RestAssured
                .given()
                .baseUri(root)
                .contentType(ContentType.JSON)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("league_name", hasItem(leagueName))
                //за силы света команда не играла на турнирах, поэтому должен быть фейл
                //.body("radiant_name",hasItem(teamSearch))
                .body("dire_name", hasItem(teamSearch))
                .log().headers()
                .when()
                .get(EndPoints.proMatches);
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        //response.prettyPrint();
        List<String> listRadiant = response.path("findAll{it.league_name == '" + leagueName + "'}.radiant_name");
        List<String> listDire = response.path("findAll{it.league_name == '" + leagueName + "'}.dire_name");
        if (listRadiant.contains(teamSearch) || listDire.contains(teamSearch))
            logger.info("Команда " + teamSearch + " найдена в лиге " + leagueName);
        else
            logger.info("Команда " + teamSearch + " не найдена в лиге " + leagueName);
        logger.info("Test over");
    }

    @Test
    public void responseGetFailNonFluentTest() {
        logger.info("Get, non fluent, fail test");
        //искомая команда
        String teamSearch = "Team Spirit";
        RequestSpecification request = RestAssured.given();
        request.baseUri(root);
        request.then().statusCode(400);
        //request.then().statusCode(200);
        request.contentType(ContentType.JSON);
        request.then().contentType("application/json");
        request.then().body("name", hasItem(teamSearch));
        request.log().method();
        request.log().headers();
        request.log().uri();
        Response response = request.get(EndPoints.proTeams);
        //проверка
        String contentType = response.header("Content-Type");
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        logger.info("Content-Type " + contentType);
        //response.prettyPrint();
        List<String> listTeams = response.jsonPath().getList("name");
        if (listTeams.contains(teamSearch))
            logger.info("Команда " + teamSearch + " найдена");
        else
            logger.info("Команда " + teamSearch + " не найдена");
        logger.info("Test over");
    }

    @Test
    public void responseGetFailFluentTest() {
        logger.info("Get, fluent, fail test");
        String teamSearch = "Team Spirit";
        Response response = RestAssured
                .given()
                .baseUri(root)
                .contentType(ContentType.JSON)
                .then()
                .statusCode(502)
                //.statusCode(200)
                .contentType("application/json")
                .body("name", hasItem(teamSearch))
                .log().headers()
                .when()
                .get(EndPoints.proTeams);
        String contentType = response.header("Content-Type");
        //response.prettyPrint();
        if (contentType.contains("json"))
            logger.info("Content-Type = JSON");
        else
            logger.info("Content-Type != JSON");
        logger.info("Content-Type " + contentType);
        List<String> listTeams = response.jsonPath().getList("name");
        if (listTeams.contains(teamSearch))
            logger.info("Команда " + teamSearch + " найдена");
        else
            logger.info("Команда " + teamSearch + " не найдена");
        logger.info("Test over");
    }

}
