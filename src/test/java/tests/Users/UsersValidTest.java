package tests.Users;

import DataProviders.UserDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import POJO.User;
import POJO.Email;
import org.testng.asserts.SoftAssert;
import utils.TestBase;
import utils.Variables;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UsersValidTest extends TestBase
{
    SoftAssert softAssert = new SoftAssert();

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

        List<HashMap<String, Object>> users = response.jsonPath().getList("");

        Assert.assertFalse(users.isEmpty(), "No users found");
        Variables.userValidId = (int) users.getFirst().get("id");
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

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that a new user can be created with valid input data.")
    @Test(dataProvider = "createUserValidData", dataProviderClass = UserDataProvider.class)
    public void TC3_CreateUser_With_ValidData(User user)
    {
        System.out.println(user.getEmail());
        Response response = given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat().log().all()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(201)
                .extract().response();

        HashMap<String, Object> createdUser = response.jsonPath().get("");

        Variables.createdUserId = (int) createdUser.get("id");
        Variables.userInValidId = Variables.createdUserId + 1;
        softAssert.assertEquals(createdUser.get("email"), user.getEmail());
        softAssert.assertEquals(createdUser.get("password"), user.getPassword());
        softAssert.assertEquals(createdUser.get("name"), user.getName());
        softAssert.assertEquals(createdUser.get("role"), user.getRole());
        softAssert.assertEquals(createdUser.get("avatar"), user.getAvatar());

        softAssert.assertAll();
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's name can be updated successfully.")
    @Test ( dependsOnMethods = "TC3_CreateUser_With_ValidData")
    public void TC4_UpdateExistingUser_NameOnly()
    {
        User updatedUser = new User();
        updatedUser.setName("Arial");

        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(updatedUser)
                .when().put("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), updatedUser.getName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's email can be updated successfully.")
    @Test ( dependsOnMethods = "TC3_CreateUser_With_ValidData")
    public void TC5_UpdateExistingUser_EmailOnly()
    {
        User updatedUser = new User();
        updatedUser.setEmail("adam78@mail.com");

        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(updatedUser)
                .when().put("/users/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("email"), updatedUser.getEmail());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user's password can be updated successfully.")
    @Test ( dependsOnMethods = "TC3_CreateUser_With_ValidData")
    public void TC6_UpdateExistingUser_PasswordOnly()
    {
        User updatedUser = new User();
        updatedUser.setPassword("abcd123");

        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(updatedUser)
                .when().put("/users/{id}")
                .then()
                .assertThat().log().all()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("password"), updatedUser.getPassword());
    }

    // Bug
    @Severity(SeverityLevel.MINOR)
    @Test (dependsOnMethods = "TC3_CreateUser_With_ValidData")
    @Description("Verify that an email is not available for registration.")
    public void TC7_CheckEmailAvailability_ExistingEmail()
    {
        Email validEmail = new Email();
        validEmail.setEmail(Variables.userEmail);

        Response response = given()
                .header("Content-Type", "application/json")
                .body(validEmail)
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
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an email is available for registration.")
    public void TC8_CheckEmailAvailability_NonExistentEmail()
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
        Assert.assertFalse(response.jsonPath().getBoolean("isAvailable"));
    }
}
