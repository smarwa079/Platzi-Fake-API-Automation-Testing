package tests.AuthJWT;

import DataProviders.AuthDataProvider;
import POJO.Login;
import POJO.RefreshToken;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

import org.testng.annotations.Test;
import utils.TestBase;
import utils.Variables;

import static io.restassured.RestAssured.given;


public class AuthInvalidTest extends TestBase
{
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with invalid password returns 401 Unauthorized.")
    @Test (dataProvider = "loginInValidPassword", dataProviderClass = AuthDataProvider.class)
    public void TC1_Login_With_InValidPassword(Login login)
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
    public void TC2_Login_With_NonExistentUser(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with missing password returns 401 Unauthorized.")
    public void TC3_Login_With_MissingPassword()
    {
        Login login = new Login();
        login.setEmail(Variables.userEmail);

        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify login with missing email returns 401 Unauthorized.")
    public void TC4_Login_With_MissingEmail()
    {
        Login login = new Login();
        login.setPassword("root");

        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().post("/auth/login")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Verify refresh token request without token returns 401 Unauthorized.")
    @Test (dataProvider = "loginValidData", dataProviderClass = AuthDataProvider.class)
    public void TC5_RetrieveUserProfile_With_InValidToken(Login login)
    {
        String token = "invalidToken";

        given()
                .header("Authorization", "Bearer "+token)
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
    public void TC6_RetrieveUserProfile_Without_AccessToken(Login login)
    {
        given()
                .header("Content-Type", "application/json")
                .body(login)
                .when().get("/auth/profile")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    @Severity (SeverityLevel.NORMAL)
    @Description ("Verify refresh request with empty or malformed body returns 400 Bad Request.")
    public void TC7_InValidRefreshAccessToken()
    {
        RefreshToken refresh = new RefreshToken();
        refresh.setRefreshToken("invalidToken");

        given()
                .header("Content-Type", "application/json")
                .body(refresh)
                .when().post("/auth/refresh-token")
                .then()
                .assertThat()
                .statusCode(401);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify refresh token request without token returns 400 Bad Request.")
    public void TC8_RefreshToken_Without_Token()
    {
        given()
                .header("Content-Type", "application/json")
                .body("{}")
                .when().post("/auth/refresh-token")
                .then()
                .assertThat()
                .statusCode(400);
    }
}
