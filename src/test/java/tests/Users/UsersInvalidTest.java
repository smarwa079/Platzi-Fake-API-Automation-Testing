package tests.Users;

import DataProviders.UserDataProvider;
import POJO.Email;
import POJO.User;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import utils.TestBase;
import utils.Variables;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UsersInvalidTest extends TestBase
{
    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when retrieving a non-existent user.")
    public void TC1_GetUser_Using_InValidID()
    {
        given()
                .pathParam("id", Variables.userInValidId)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when retrieving a user with a non-numeric ID.")
    public void TC2_GetUser_Using_NonNumericId()
    {
        String id = "abc";

        given()
                .pathParam("id", id)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    // Bug
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when trying to create a user that already exists.")
    @Test(dataProvider = "createUserValidData", dataProviderClass = UserDataProvider.class)
    public void TC3_CreateExistingUser(User user) {

        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    // Bug
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an error is returned when trying to create a user with missing name.")
    @Test(dataProvider = "createUserMissingNameData", dataProviderClass = UserDataProvider.class)
    public void TC4_CreateUser_With_MissingName(User user)
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
    public void TC5_CreateUser_With_MissingEmail(User user)
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
    public void TC6_CreateUser_With_MissingPassword(User user)
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
    public void TC7_CreateUser_With_MissingAvatar(User user)
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
    public void TC8_CreateUser_With_InvalidEmailFormat(User user)
    {
        given()
                .header("Content-Type", "application/json")
                .body(user)
                .when().post("/users/")
                .then()
                .assertThat()
                .statusCode(400);
    }
    
    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when trying to update an existing user with an invalid email format.")
    public void TC9_UpdateExistingUser_InvalidEmailFormat()
    {
        User user = new User();
        user.setEmail("gabriel");

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that an existing user can be updated successfully.")
    public void TC10_UpdateExistingUser_With_NoBody()
    {
        User user = new User();

        given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdUserId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when trying to update a non-existent user.")
    public void TC11_UpdateNonExistentUser()
    {
        User user = new User();
        user.setName("unknown user");
        user.setEmail("unknown@gmail.com");

        System.out.println(Variables.userInValidId);
        System.out.println(Variables.createdUserId);
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.userInValidId)
                .body(user)
                .when().get("/users/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify that an error is returned when checking the availability of an email with an invalid format.")
    public void TC12_CheckEmailAvailability_InvalidEmailFormat()
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
    public void TC13_CheckEmailAvailability_NoBody()
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
