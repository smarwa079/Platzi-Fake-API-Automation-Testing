package tests;

import DataProviders.AuthDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import POJO.Login;
import POJO.RefreshToken;
import utils.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AuthTest extends TestBase {

    @Severity (SeverityLevel.CRITICAL)
    @Description ("Verify successful login returns valid access and refresh tokens with correct structure.")
    @Test (dataProvider = "loginValidData", dataProviderClass = AuthDataProvider.class)
    public void TC1_Login_With_ValidCredentials(Login login)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("login-response-schema.json"))
                .statusCode(201)
                .extract()
                .response();
        AuthDataProvider.accessToken = response.jsonPath().getString("access_token");
        AuthDataProvider.refreshToken = response.jsonPath().getString("refresh_token");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with invalid password returns 401 Unauthorized.")
    @Test (dataProvider = "loginInValidPassword", dataProviderClass = AuthDataProvider.class)
    public void TC2_Login_With_InValidPassword(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with non-existent user returns 401 Unauthorized.")
    @Test (dataProvider = "loginInValidEmail", dataProviderClass = AuthDataProvider.class)
    public void TC3_Login_With_NonExistentUser(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with missing password returns 400 Bad Request.")
    @Test (dataProvider = "loginMissingPassword", dataProviderClass = AuthDataProvider.class)
    public void TC4_Login_With_MissingPassword(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with missing email returns 400 Bad Request.")
    @Test (dataProvider = "loginMissingEmail", dataProviderClass = AuthDataProvider.class)
    public void TC5_Login_With_MissingEmail(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Verify user profile is accessible with a valid access token.")
    @Test (dependsOnMethods = "TC1_Login_With_ValidCredentials", dataProvider = "loginValidData", dataProviderClass = AuthDataProvider.class)
    public void TC6_RetrieveUserProfile_With_ValidData(Login login)
    {
        Response response = given()
                .header("Authorization", "Bearer " + AuthDataProvider.accessToken)
                .header("Content-Type", "application/json")
                .body(login)
                .when().get("/auth/profile")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("email"), login.getEmail());
        Assert.assertEquals(response.jsonPath().getString("password"), login.getPassword());
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Verify refresh token request without token returns 401 Unauthorized.")
    @Test (dataProvider = "loginValidData", dataProviderClass = AuthDataProvider.class)
    public void TC7_RetrieveUserProfile_With_InValidToken(Login login)
    {
        given()
                .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.")
                .header("Content-Type", "application/json")
                .body(login)
                .when().get("/auth/profile")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Severity (SeverityLevel.CRITICAL)
    @Description ("Verify request without access token returns 401 Unauthorized.")
    @Test (dataProvider = "loginValidData", dataProviderClass = AuthDataProvider.class)
    public void TC8_RetrieveUserProfile_Without_AccessToken(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().get("/auth/profile")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Severity (SeverityLevel.CRITICAL)
    @Description ("Verify new tokens are issued using a valid refresh token.")
    @Test (dependsOnMethods = "TC6_RetrieveUserProfile_With_ValidData", dataProvider = "validRefreshToken", dataProviderClass = AuthDataProvider.class)
    public void TC9_ValidRefreshAccessToken(RefreshToken refresh)
    {
        given()
                .header("Content-Type", "application/json")
                .body(refresh)
                .when().post("/auth/refresh-token")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("login-response-schema.json"))
                .statusCode(201);
    }

    @Severity (SeverityLevel.NORMAL)
    @Description ("Verify refresh request with empty or malformed body returns 400 Bad Request.")
    @Test (dataProvider = "inValidRefreshToken", dataProviderClass = AuthDataProvider.class)
    public void TC10_InValidRefreshAccessToken(RefreshToken refresh)
    {
        given()
                .header("Content-Type", "application/json")
                .body(refresh)
                .when().post("/auth/refresh-token")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
