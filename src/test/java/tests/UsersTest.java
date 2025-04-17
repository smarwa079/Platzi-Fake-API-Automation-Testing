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
import utils.Variables;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UsersTest extends TestBase
{
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all users can be retrieved successfully.")
    public void TC1_GetAllUsers_Successfully() {

        Response response = given()
                .when().get("/users")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "No users found");

        Variables.userValidId = response.jsonPath().getInt("[0].id");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that a user can be retrieved using a valid ID.")
    public void TC2_GetUser_Using_ValidID()
    {
        given()
                .pathParam("id", Variables.userValidId)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when retrieving a non-existent user.")
    public void TC3_GetUser_Using_InValidID()
    {
        int id = Variables.userValidId - 1;

        given()
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when retrieving a user with a non-numeric ID.")
    public void TC4_GetUser_Using_NonNumericId()
    {
        String id = "abc";

        given()
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a new user can be created with valid input data.")
    @Test(dataProvider = "createUserValidData", dataProviderClass = UserDataProvider.class)
    public void TC5_CreateUser_With_ValidData(User user) {

        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(201)
                .extract().response();

        Variables.createdUserId = response.jsonPath().getInt("id");

        Assert.assertEquals(response.jsonPath().getString("email"), user.getEmail());
        Assert.assertEquals(response.jsonPath().getString("password"), user.getPassword());
        Assert.assertEquals(response.jsonPath().getString("name"), user.getName());
        Assert.assertEquals(response.jsonPath().getString("role"), user.getRole());
        Assert.assertEquals(response.jsonPath().getString("avatar"), user.getAvatar());
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when trying to create a user that already exists.")
    @Test(dataProvider = "createUserValidData", dataProviderClass = UserDataProvider.class)
    public void TC6_CreateExistingUser(User user) {

        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing name.")
    @Test(dataProvider = "createUserMissingNameData", dataProviderClass = UserDataProvider.class)
    public void TC7_CreateUser_With_MissingName(User user)
    {
        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing email.")
    @Test(dataProvider = "createUserMissingEmailData", dataProviderClass = UserDataProvider.class)
    public void TC8_CreateUser_With_MissingEmail(User user)
    {
        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing email.")
    @Test(dataProvider = "createUserMissingPasswordData", dataProviderClass = UserDataProvider.class)
    public void TC9_CreateUser_With_MissingPassword(User user)
    {
        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing email.")
    @Test(dataProvider = "createUserMissingAvatarData", dataProviderClass = UserDataProvider.class)
    public void TC10_CreateUser_With_MissingAvatar(User user)
    {
        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing email.")
    @Test(dataProvider = "createUserMissingEmailData", dataProviderClass = UserDataProvider.class)
    public void TC11_CreateUser_With_InvalidEmailFormat(User user)
    {
        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's name can be updated successfully.")
    @Test ( dependsOnMethods = "TC5_CreateUser_With_ValidData", dataProvider = "updateNameForExistingUser", dataProviderClass = UserDataProvider.class)
    public void TC12_UpdateExistingUser_NameOnly(User user)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), user.getName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's email can be updated successfully.")
    @Test ( dependsOnMethods = "TC5_CreateUser_With_ValidData", dataProvider = "updateEmailForExistingUser", dataProviderClass = UserDataProvider.class)
    public void TC13_UpdateExistingUser_EmailOnly(User user)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), user.getName());
    }

    @Severity(SeverityLevel.MINOR)
    @Test (dependsOnMethods = "TC5_CreateUser_With_ValidData")
    @Description("Verify that an error is returned when trying to update an existing user with an invalid email format.")
    public void TC14_UpdateExistingUser_InvalidEmailFormat()
    {
        User user = new User();
        user.setEmail("gabriel@");

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's password can be updated successfully.")
    @Test ( dependsOnMethods = "TC5_CreateUser_With_ValidData", dataProvider = "updatePasswordForExistingUser", dataProviderClass = UserDataProvider.class)
    public void TC15_UpdateExistingUser_PasswordOnly(User user)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("password"), user.getPassword());
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user can be updated successfully.")
    public void TC16_UpdateExistingUser_With_NoBody()
    {
        User user = new User();

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
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
    public void TC17_UpdateNonExistentUser(User user, int userId)
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
    public void TC18_CheckEmailAvailability_AvailableEmail(Email email)
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

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when checking the availability of an unavailable email.")
    public void TC19_CheckEmailAvailability_UnAvailableEmail()
    {
        Email email = new Email();
        email.setEmail("mansour@mail.com");

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

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when checking the availability of an email with an invalid format.")
    public void TC20_CheckEmailAvailability_InvalidEmailFormat()
    {
        Email email = new Email();
        email.setEmail("mansour@");

        given()
                .header("Content-Type", "application/json")
                .body(email)
                .when().post("/users/is-available")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when checking the availability of an email with a missing body.")
    public void TC19_CheckEmailAvailability_NoBody()
    {
        Email email = new Email();

        given()
                .header("Content-Type", "application/json")
                .body(email)
                .when().post("/users/is-available")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
