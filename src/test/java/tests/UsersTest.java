package tests;

import DataProviders.UserDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import POJO.User;
import POJO.Email;
import utils.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UsersTest extends TestBase {

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all users can be retrieved successfully.")
    public void TC1_GetAllUsers() {

        given()
                .when().get("/users")
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a user can be retrieved using a valid ID.")
    public void TC2_GetUser_Using_ValidID()
    {
        int id = 9;

        given()
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then().log().all()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when retrieving a non-existent user.")
    public void TC3_GetUser_Using_InValidID()
    {
        int id = 150;

        Response  response = given()
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a new user can be created with valid input data.")
    @Test(dataProvider = "createUserValidData", dataProviderClass = UserDataProvider.class)
    public void TC4_CreateUser_With_ValidData(User user) {

        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(201)
                .extract().response();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing fields.")
    @Test(dataProvider = "createUserInValidData", dataProviderClass = UserDataProvider.class)
    public void TC5_CreateUser_With_InValidData(User user) {

        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400)
                .extract().response();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user can be updated successfully.")
    @Test ( dependsOnMethods = "TC4_CreateUser_With_ValidData", dataProvider = "updateExistingUserValidData", dataProviderClass = UserDataProvider.class)
    public void TC6_UpdateExistingUser(User user, int userId)
    {
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", userId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200);
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when trying to update a non-existent user.")
    @Test (dataProvider = "updateNonExistentUserData", dataProviderClass = UserDataProvider.class)
    public void TC7_UpdateNonExistentUser(User user, int userId)
    {
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", userId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test ( dataProvider = "emailAvailabilityValidData", dataProviderClass = UserDataProvider.class)
    public void TC8_CheckEmailAvailability_ValidTest(Email email)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(email)
                .when().post("/users/is-available")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("email-checker-response-schema.json"))
                .statusCode(201)
                .extract()
                .response();
        Assert.assertNotEquals(response.jsonPath().getBoolean("isAvailable"), true);
    }

    @Test ( dataProvider = "emailAvailabilityInValidData", dataProviderClass = UserDataProvider.class)
    public void TC9_CheckEmailAvailability_InValidTest(Email email)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(email)
                .when().post("/users/is-available")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("email-checker-response-schema.json"))
                .statusCode(201)
                .extract()
                .response();
        Assert.assertTrue(response.jsonPath().getBoolean("isAvailable"));
    }
}
