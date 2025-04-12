package tests;

import DataProviders.CategoryDataProvider;
import POJO.Category;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class CategoriesTest extends TestBase {

    int categoryValidId;
    int createdCategoryId;
    String categoryValidSlug;
    int numberOfCategories;

    @Test (priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all categories can be fetched successfully")
    public void TC1_GetAllCategories()
    {
        Response response = given()
                .when().get("/categories")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "List should not be empty");

        numberOfCategories = response.jsonPath().getList("$").size();
        categoryValidId = response.jsonPath().getInt("[0].id");
        categoryValidSlug = response.jsonPath().getString("[0].slug");
    }

    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify fetching categories with a valid limit returns the correct number of categories")
    public void TC2_GetCategories_With_ValidLimit()
    {
        int limit = ((numberOfCategories - 4) <= 0)? numberOfCategories: numberOfCategories - 4;

        Response response = given()
                .queryParam("limit", limit)
                .when().get("/categories")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getList("$").size(), limit);
    }

    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching categories with an invalid limit returns a 400 status code")
    public void TC3_GetCategories_With_InValidLimit()
    {
        int limit = numberOfCategories + 1;

        given()
                .queryParam("limit", limit)
                .when().get("/categories")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Description("Verify fetching category by valid ID returns the correct category with valid schema")
    public void TC4_GetCategories_With_ValidID()
    {
        Response response = given()
                .pathParam("id", categoryValidId)
                .when().get("/categories/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("category-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getInt("id"), categoryValidId);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching category by invalid ID returns a 400 status code")
    public void TC5_GetCategory_With_InvalidId()
    {
        int catId = -1;

        given()
                .pathParam("id", catId)
                .when().get("/categories/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching category by invalid ID format returns a 400 status code")
    public void TC6_GetCategory_With_InvalidIdFormat()
    {
        String catId = "abc";

        given()
                .pathParam("id", catId)
                .when().get("/categories/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Description("Verify fetching category by valid slug returns correct category and valid schema")
    public void TC7_GetCategories_With_ValidSlug()
    {
        Response response = given()
                .pathParam("slug", categoryValidSlug)
                .when().get("/categories/slug/{slug}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("category-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("slug"), categoryValidSlug);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching category by invalid slug returns 400 status code")
    public void TC8_GetCategories_With_InValidSlug()
    {
        String catSlug = "testslug";

        given()
                .pathParam("slug", catSlug)
                .when().get("/categories/slug/{slug}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify a new category is created successfully with valid data")
    @Test (dataProvider = "createCategoryValidData", dataProviderClass = CategoryDataProvider.class)
    public void TC9_CreateCategory_Successfully(Category category)
    {
        Response response = given()
                .contentType("application/json")
                .body(category)
                .when().post("/categories/")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("category-schema.json"))
                .statusCode(201)
                .extract()
                .response();

        createdCategoryId = response.jsonPath().getInt("id");

        String name = response.jsonPath().getString("name");
        String image = response.jsonPath().getString("image");

        Assert.assertEquals(name, category.getName());
        Assert.assertEquals(image, category.getImage());
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify creating an already existing category fails with 400 status code")
    @Test (dependsOnMethods = "TC9_CreateCategory_Successfully", dataProvider = "createCategoryValidData", dataProviderClass = CategoryDataProvider.class)
    public void TC10_CreateExistentCategory(Category category)
    {
        given()
                .contentType("application/json")
                .body(category)
                .when().post("categories/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify creating a category with invalid image link fails")
    @Test (dataProvider = "createCategoryInValidImageLink", dataProviderClass = CategoryDataProvider.class)
    public void TC11_CreateCategory_With_InValidImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .body(category)
                .when().post("categories/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creating a category without name fails")
    @Test (dataProvider = "createCategoryWithoutName", dataProviderClass = CategoryDataProvider.class)
    public void TC12_CreateCategory_Without_Name(Category category)
    {
        given()
                .contentType("application/json")
                .body(category)
                .when().post("categories/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creating a category without image link fails")
    @Test (dataProvider = "createCategoryWithoutImageLink", dataProviderClass = CategoryDataProvider.class)
    public void TC13_CreateCategory_Without_ImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .body(category)
                .when().post("categories/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify updating an existing category with valid data works successfully")
    @Test (dependsOnMethods = "TC9_CreateCategory_Successfully", dataProvider = "updateExistingCategoryValidData", dataProviderClass = CategoryDataProvider.class)
    public void TC14_UpdateExistingCategory_ValidData(Category category)
    {
        Response response = given()
                .contentType("application/json")
                .pathParam("id", createdCategoryId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .body(matchesJsonSchemaInClasspath("category-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        String name = response.jsonPath().getString("name");
        String image = response.jsonPath().getString("image");

        Assert.assertEquals(name, category.getName());
        Assert.assertEquals(image, category.getImage());
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a category with invalid image link fails")
    @Test (dependsOnMethods = "TC1_GetAllCategories", dataProvider = "updateExistingCategoryInValidImageLink", dataProviderClass = CategoryDataProvider.class)
    public void TC15_UpdateExistingCategory_InValidImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", categoryValidId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a category with only image link fails")
    @Test (dependsOnMethods = "TC1_GetAllCategories", dataProvider = "updateExistingCategoryOnlyImageLink", dataProviderClass = CategoryDataProvider.class)
    public void TC16_UpdateExistingCategory_OnlyImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", categoryValidId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a category with only name fails")
    @Test (dataProvider = "updateExistingCategoryOnlyName", dataProviderClass = CategoryDataProvider.class)
    public void TC17_UpdateExistingCategory_OnlyName(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", categoryValidId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify updating a non-existent category fails with 400 status code")
    @Test (dataProvider = "updateNonExistentCategory", dataProviderClass = CategoryDataProvider.class)
    public void TC18_UpdateNonExistentCategory(Category category)
    {
        int catId = categoryValidId-1;

        given()
                .contentType("application/json")
                .pathParam("id", catId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test (dependsOnMethods = "TC9_CreateCategory_Successfully")
    @Description("Verify successfully deleting an existing category")
    public void TC19_DeleteExistingCategory()
    {
        given()
                .contentType("application/json")
                .pathParam("id", createdCategoryId)
                .when().delete("/categories/{id}")
                .then()
                .body(equalTo("true"))
                .statusCode(200);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify deleting a non-existent category returns 200 with body 'true'")
    public void TC20_DeleteNonExistentCategory()
    {
        int catId = 100;

        given()
                .contentType("application/json")
                .pathParam("id", catId)
                .when().delete("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Description("Verify fetching products for a valid category ID returns 200")
    public void TC21_GetProducts_For_ValidCategoryID()
    {
        Response response = given()
                .contentType("application/json")
                .pathParam("id", categoryValidId)
                .when().get("/categories/{id}/products")
                .then()
                .statusCode(200)
                .extract()
                .response();
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching products for an invalid category ID returns 400")
    public void TC22_GetProducts_For_InValidCategoryID()
    {
        int catId = -1;

        given()
                .contentType("application/json")
                .pathParam("id", catId)
                .when().get("/categories/{id}/products")
                .then()
                .statusCode(404);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching products without providing a category ID returns 400")
    public void TC23_GetProducts_Without_CategoryID()
    {
        given()
                .contentType("application/json")
                .when().get("/categories/{id}/products")
                .then()
                .statusCode(400);
    }
}
