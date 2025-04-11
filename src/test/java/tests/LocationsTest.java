package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class LocationsTest extends TestBase {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all locations can be retrieved successfully.")
    public void TC1_GetAllLocations() {

        Response response = given()
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getList("$").size(), 10);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that locations are filtered by a valid origin")
    public void TC2_GetLocation_By_ValidOrigin()
    {
        String origin = "10.000000,10.000000";

        given()
                .queryParam("origin", origin)
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that filtering locations by an invalid origin returns an error message.")
    public void TC3_GetLocation_By_InValidOrigin()
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
        Assert.assertEquals(response.jsonPath().getList("message").get(0), "origin must be a latitude,longitude string");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that filtering locations by only one coordinate origin returns an error message.")
    public void TC4_GetLocation_By_OnlyOneCoordinateOrigin()
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
    public void TC5_GetLocations_By_ValidLimit()
    {
        int size = 4;

        Response response = given()
                .queryParam("size", size)
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getList("$").size(), size);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that limiting results returns only the specified number of locations.")
    public void TC6_GetLocations_By_InValidLimit()
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

        Assert.assertTrue(response.jsonPath().getList("$").isEmpty());
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that nearby locations within a radius are returned correctly for valid coordinates.")
    public void TC7_GetLocations_By_ValidRadius()
    {

        String origin = "4.62610605337789,-74.31904314364516";
        int radius = 8;

        given()
                .queryParam("origin", origin)
                .queryParam("radius", radius)
                .when().get("/locations")
                .then()
                .assertThat()
                .statusCode(200);
    }
}
