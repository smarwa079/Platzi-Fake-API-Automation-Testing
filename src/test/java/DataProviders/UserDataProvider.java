package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.User;
import POJO.Email;
import utils.Variables;

public class UserDataProvider
{
    @DataProvider (name = "createUserValidData")
    public static User[] createUserValidData() {

        User validUser = new User();
        validUser.setName(Variables.userName);
        validUser.setEmail(Variables.userEmail);
        validUser.setPassword(Variables.userPassword);
        validUser.setRole("admin");
        validUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                validUser
        };
    }

    @DataProvider (name = "createUserMissingNameData")
    public static User[] createUserMissingNameData() {

        User inValidUser = new User();

        inValidUser.setEmail(Variables.faker.internet().emailAddress());
        inValidUser.setPassword("123456");
        inValidUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "createUserMissingEmailData")
    public static User[] createUserMissingEmailData() {

        User inValidUser = new User();

        inValidUser.setName(Variables.faker.name().username());
        inValidUser.setPassword("123456");
        inValidUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "createUserMissingPasswordData")
    public static User[] createUserMissingPasswordData() {

        User inValidUser = new User();

        inValidUser.setName(Variables.faker.name().username());
        inValidUser.setEmail(Variables.faker.internet().emailAddress());
        inValidUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "createUserMissingAvatarData")
    public static User[] createUserMissingAvatarData() {

        User inValidUser = new User();

        inValidUser.setName(Variables.faker.name().username());
        inValidUser.setEmail(Variables.faker.internet().emailAddress());
        inValidUser.setPassword("123456");

        return new User[] {
                inValidUser
        };
    }
}
