package DataProviders;

import org.testng.annotations.DataProvider;
import POJO.User;
import POJO.Email;

public class UserDataProvider {

    public static int createdUserIds;

    @DataProvider (name = "createUserValidData")
    public static User[] createUserValidData() {

        User validUser = new User();

        validUser.setName("zoe");
        validUser.setEmail("zoe@gmail.com");
        validUser.setPassword("123456");
        validUser.setAvatar("https://i.ibb.co/9y2392F/test-product-image.jpg");

        return new User[] {
                validUser
        };
    }

    @DataProvider (name = "createUserInValidData")
    public static User[] createUserInValidData() {

        User inValidUser = new User();

        inValidUser.setName("john");
        inValidUser.setEmail("john@gmail.com");
        inValidUser.setPassword("123456");

        return new User[] {
                inValidUser
        };
    }

    @DataProvider (name = "updateExistingUserValidData")
    public static Object[][]  updateExistingUserValidData()
    {
        User updatedUser = new User();

        updatedUser.setName("Arial");
        updatedUser.setEmail("john@mail.com");

        return new Object[][]
                {
                        {
                            updatedUser,
                            createdUserIds
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

    @DataProvider (name = "emailAvailabilityInValidData")
    public static Email[] emailAvailabilityInValidData()
    {
        Email email3 = new Email();

        email3.setEmail("mansour@mail.com");

        return new Email[]
                {
                        email3
                };
    }
}
