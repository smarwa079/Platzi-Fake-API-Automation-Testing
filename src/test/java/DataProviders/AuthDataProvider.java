package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.Login;
import utils.Variables;

public class AuthDataProvider
{

    @DataProvider (name = "loginValidData")
    public static Login[] loginValidData()
    {
        Login validLogin = new Login();

        validLogin.setEmail(Variables.userEmail);
        validLogin.setPassword(Variables.userPassword);

        return new Login[] {
                validLogin
        };
    }

    @DataProvider (name = "loginInValidPassword")
    public static Login[] loginInValidPassword()
    {
        Login invalidLogin = new Login();

        invalidLogin.setEmail(Variables.userEmail);
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
        invalidLogin.setPassword(Variables.userPassword);

        return new Login[] {
                invalidLogin
        };
    }
}
