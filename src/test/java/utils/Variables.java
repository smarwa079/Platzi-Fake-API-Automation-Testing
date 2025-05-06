package utils;

import com.github.javafaker.Faker;

public class Variables
{
    public static Faker faker = new Faker();

    public static String categoryName = faker.commerce().department();
    public static String categoryImage = "https://imgur.com/SvL0zaZ";
    public static int categoryValidId = 0;
    public static int createdCategoryId = 0;
    public static String categoryValidSlug = "";
    public static int numberOfCategories = 0;
    public static int categoryValidLimit = (int) Math.floor(Math.random() * numberOfCategories) + 1;
    public static int categoryExceedingLimit = categoryValidLimit + 1;
    public static int categoryInValidId = createdCategoryId + 200;

    public static String productTitle = faker.commerce().productName();
    public static String productImage = "https://i.ibb.co/9y2392F/test-product-image.jpg";
    public static int createdProductId = 0;
    public static int productValidId = 0;
    public static String productValidSlug = "";
    public static int productInValidId = createdProductId + 200;

    public static String userName = faker.name().username();
    public static String userEmail = faker.internet().emailAddress();
    public static String userPassword = "123456";
    public static int userValidId = 0;
    public static int createdUserId = 0;
    public static int userInValidId = 0;

    public static String accessToken = "";
    public static String refreshToken = "";

}
