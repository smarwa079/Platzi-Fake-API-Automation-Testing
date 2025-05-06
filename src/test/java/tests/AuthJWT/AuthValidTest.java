package tests.AuthJWT;

import DataProviders.AuthDataProvider;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import POJO.Login;
import POJO.RefreshToken;
import org.testng.asserts.SoftAssert;
import utils.TestBase;
import utils.Variables;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AuthValidTest extends TestBase
{
    SoftAssert softAssert = new SoftAssert();

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
        Variables.accessToken = response.jsonPath().getString("access_token");
        Variables.refreshToken = response.jsonPath().getString("refresh_token");
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Verify user profile is accessible with a valid access token.")
    @Test (dependsOnMethods = "TC1_Login_With_ValidCredentials", dataProvider = "loginValidData", dataProviderClass = AuthDataProvider.class)
    public void TC2_RetrieveUserProfile_With_ValidData(Login login)
    {
        Response response = given()
                .header("Authorization", "Bearer " + Variables.accessToken)
                .header("Content-Type", "application/json")
                .body(login)
                .when().get("/auth/profile")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("user-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        softAssert.assertEquals(response.jsonPath().getString("email"), login.getEmail());
        softAssert.assertEquals(response.jsonPath().getString("password"), login.getPassword());

        softAssert.assertAll("User profile data validation failed");
    }

    @Severity (SeverityLevel.CRITICAL)
    @Description ("Verify new tokens are issued using a valid refresh token.")
    @Test
    public void TC3_ValidRefreshAccessToken()
    {
        RefreshToken refresh = new RefreshToken();
        refresh.setRefreshToken(Variables.refreshToken);

        given()
                .header("Content-Type", "application/json")
                .body(refresh)
                .when().post("/auth/refresh-token")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("login-response-schema.json"))
                .statusCode(201);
    }
}
