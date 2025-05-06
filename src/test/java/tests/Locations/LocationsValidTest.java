package tests.Locations;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestBase;

import static io.restassured.RestAssured.given;

public class LocationsValidTest extends TestBase {

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

        Assert.assertEquals(response.jsonPath().getList("").size(), 10);
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
    @Description("Verify that limiting results returns only the specified number of locations.")
    public void TC3_GetLocations_By_ValidLimit()
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

        Assert.assertEquals(response.jsonPath().getList("").size(), size);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that nearby locations within a radius are returned correctly for valid coordinates.")
    public void TC4_GetLocations_By_ValidRadius()
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
