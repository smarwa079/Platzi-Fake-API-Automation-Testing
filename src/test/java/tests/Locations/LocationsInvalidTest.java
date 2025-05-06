package tests.Locations;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestBase;

import static io.restassured.RestAssured.given;

public class LocationsInvalidTest extends TestBase
{

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that filtering locations by an invalid origin returns an error message.")
    public void TC1_GetLocation_By_InValidOrigin()
    {
        String origin = "91,181";

        Response response = given()
                .queryParam("origin", origin)
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();
        Assert.assertEquals(response.jsonPath().getList("message").getFirst(), "origin must be a latitude,longitude string");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that filtering locations by only one coordinate origin returns an error message.")
    public void TC2_GetLocation_By_OnlyOneCoordinateOrigin()
    {
        String origin = "45.0";

        Response response = given()
                .queryParam("origin", origin)
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getList("message").get(0), "origin must be a latitude,longitude string");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that limiting results returns only the specified number of locations.")
    public void TC3_GetLocations_By_InValidLimit()
    {
        int size = -1;

        Response response = given()
                .queryParam("size", size)
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertTrue(response.jsonPath().getList("").isEmpty());
    }
}
