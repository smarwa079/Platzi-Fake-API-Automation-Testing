package tests.Categories;

import DataProviders.CategoryDataProvider;
import POJO.Category;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import utils.TestBase;
import utils.Variables;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class CategoriesValidTest extends TestBase
{
    SoftAssert softAssert = new SoftAssert();

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

        List<HashMap<String, Object>> categories = response.jsonPath().getList("");

        Assert.assertFalse(categories.isEmpty(), "List should not be empty");

        Variables.numberOfCategories = categories.size();
        Variables.categoryValidId = (int) categories.getFirst().get("id");
        Variables.categoryValidSlug = categories.getFirst().get("slug").toString();
    }

    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify fetching categories with a valid limit returns the correct number of categories")
    public void TC2_GetCategories_With_ValidLimit()
    {
        Response response = given()
                .queryParam("limit", Variables.categoryValidLimit)
                .when().get("/categories")
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getList("$").size(), Variables.categoryValidLimit);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Description("Verify fetching category by valid ID returns the correct category with valid schema")
    public void TC3_GetCategories_With_ValidID()
    {
        Response response = given()
                .pathParam("id", Variables.categoryValidId)
                .when().get("/categories/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("category-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getInt("id"), Variables.categoryValidId);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Description("Verify fetching category by valid slug returns correct category and valid schema")
    public void TC4_GetCategories_With_ValidSlug()
    {
        Response response = given()
                .pathParam("slug", Variables.categoryValidSlug)
                .when().get("/categories/slug/{slug}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("category-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("slug"), Variables.categoryValidSlug);
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify a new category is created successfully with valid data")
    @Test (dataProvider = "createCategoryValidData", dataProviderClass = CategoryDataProvider.class)
    public void TC5_CreateCategory_Successfully(Category category)
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


        HashMap<String, Object> createdCategory = response.jsonPath().get("");

        Variables.createdCategoryId = (int) createdCategory.get("id");

        softAssert.assertEquals(createdCategory.get("name"), category.getName());
        softAssert.assertEquals(createdCategory.get("image"), category.getImage());

        softAssert.assertAll("Validation failed for created category");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify updating an existing category with valid data works successfully")
    @Test (dependsOnMethods = "TC5_CreateCategory_Successfully", dataProvider = "updateExistingCategoryValidData", dataProviderClass = CategoryDataProvider.class)
    public void TC6_UpdateExistingCategory_ValidData(Category category)
    {
        Response response = given()
                .contentType("application/json")
                .pathParam("id", Variables.createdCategoryId)
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

        Variables.categoryName = name;
        Variables.categoryImage = image;
    }

    @Severity(SeverityLevel.NORMAL)
    @Test (dependsOnMethods = "TC1_GetAllCategories")
    @Description("Verify fetching products for a valid category ID returns 200")
    public void TC7_GetProducts_For_ValidCategoryID()
    {
        Response response = given()
                .contentType("application/json")
                .pathParam("id", Variables.categoryValidId)
                .when().get("/categories/{id}/products")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "List should not be empty");
    }
}
