package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.Login;
import POJO.RefreshToken;
import utils.Variables;

public class AuthDataProvider
{

    @DataProvider (name = "loginValidData")
    public static Login[] loginValidData()
    {
        Login validLogin = new Login();

        validLogin.setEmail(UserDataProvider.userEmail);
        validLogin.setPassword(UserDataProvider.userPassword);

        return new Login[] {
                validLogin
        };
    }

    @DataProvider (name = "loginInValidPassword")
    public static Login[] loginInValidPassword()
    {
        Login invalidLogin = new Login();

        invalidLogin.setEmail(UserDataProvider.userEmail);
        invalidLogin.setPassword("root555");

        return new Login[] {
                invalidLogin
        };
    }

    @DataProvider (name = "loginInValidEmail")
    public static Login[] loginInValidEmail()
    {
        Login invalidLogin = new Login();

        invalidLogin.setEmail("admin555@mail.com");
        invalidLogin.setPassword(UserDataProvider.userPassword);

        return new Login[] {
                invalidLogin
        };
    }

    @DataProvider (name = "loginMissingEmail")
    public static Login[] loginMissingEmail()
    {
        Login invalidLogin = new Login();

        invalidLogin.setPassword("root");

        return new Login[] {
                invalidLogin
        };
    }

    @DataProvider (name = "loginMissingPassword")
    public static Login[] loginMissingPassword()
    {
        Login validLogin = new Login();

        validLogin.setEmail("admin@mail.com");

        return new Login[] {
                validLogin
        };
    }

    @DataProvider (name = "validRefreshToken")
    public Object[] validRefreshToken()
    {
        RefreshToken validRefresh = new RefreshToken();
        validRefresh.setRefreshToken(Variables.refreshToken);

        return new Object[]
                {
                        validRefresh
                };
    }

    @DataProvider (name = "inValidRefreshToken")
    public Object[] inValidRefreshToken()
    {
        RefreshToken validRefresh = new RefreshToken();
        validRefresh.setRefreshToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.");

        return new Object[]
                {
                        validRefresh
                };
    }
}
