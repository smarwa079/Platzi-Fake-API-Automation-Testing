package utils;

public class Variables
{
    public static int createdProductId;
    public static int productValidId;
    public static String productValidSlug;

    public static int categoryValidId;
    public static int createdCategoryId;
    public static String categoryValidSlug;
    public static int numberOfCategories;
    public static int categoryValidLimit = (int) Math.floor(Math.random() * numberOfCategories) + 1;
    public static int categoryExceedingLimit = categoryValidLimit + 1;

    public static int userValidId;
    public static int createdUserId;

    public static String accessToken;
    public static String refreshToken;
}
