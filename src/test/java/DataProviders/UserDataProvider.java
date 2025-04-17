package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.User;
import POJO.Email;

public class UserDataProvider {

    public static String userEmail = "zoe@gmail.com";
    public static String userPassword = "123456";

    @DataProvider (name = "createUserValidData")
    public static User[] createUserValidData() {

        User validUser = new User();
        validUser.setName("zoe");
        validUser.setEmail(userEmail);
        validUser.setPassword(userPassword);
        validUser.setRole("admin");
        validUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                validUser
        };
    }

    @DataProvider (name = "createUserMissingNameData")
    public static User[] createUserMissingNameData() {

        User inValidUser = new User();

        inValidUser.setEmail("john@gmail.com");
        inValidUser.setPassword("123456");
        inValidUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "createUserMissingEmailData")
    public static User[] createUserMissingEmailData() {

        User inValidUser = new User();

        inValidUser.setName("john");
        inValidUser.setPassword("123456");
        inValidUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "createUserMissingPasswordData")
    public static User[] createUserMissingPasswordData() {

        User inValidUser = new User();

        inValidUser.setName("john");
        inValidUser.setEmail("john@gmail.com");
        inValidUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "createUserMissingAvatarData")
    public static User[] createUserMissingAvatarData() {

        User inValidUser = new User();

        inValidUser.setName("john");
        inValidUser.setEmail("john@gmail.com");
        inValidUser.setPassword("123456");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "updateNameForExistingUser")
    public static Object[][]  updateNameForExistingUser()
    {
        User updatedUser = new User();

        updatedUser.setName("Arial");
        updatedUser.setEmail("john@mail.com");

        return new Object[][]
                {
                        {
                            updatedUser
                        }
                };
    }

    @DataProvider (name = "updateEmailForExistingUser")
    public static Object[][]  updateEmailForExistingUser()
    {
        User updatedUser = new User();

        updatedUser.setEmail("adam78@mail.com");

        return new Object[][]
                {
                        {
                                updatedUser
                        }
                };
    }

    @DataProvider (name = "updatePasswordForExistingUser")
    public static Object[][]  updatePasswordForExistingUser()
    {
        User updatedUser = new User();

        updatedUser.setPassword("abcd123");

        return new Object[][]
                {
                        {
                                updatedUser
                        }
                };
    }

    @DataProvider (name = "updateNonExistentUserData")
    public static Object[][]  updateNonExistentUserData()
    {
        User updatedUser = new User();

        updatedUser.setName("Arial");
        updatedUser.setEmail("john@mail.com");

        return new Object[][]
                {
                        {
                                updatedUser,
                                200
                        }
                };
    }

    @DataProvider (name = "emailAvailabilityValidData")
    public static Email[] emailAvailabilityValidData()
    {
         Email validEmail = new Email();

        validEmail.setEmail("john@mail.com");

        return new Email[]
                {
                        validEmail
                };
    }
}
