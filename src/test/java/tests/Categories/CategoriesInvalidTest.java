package tests.Categories;

import DataProviders.CategoryDataProvider;
import POJO.Category;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import utils.TestBase;
import utils.Variables;

import static io.restassured.RestAssured.given;

public class CategoriesInvalidTest extends TestBase
{
    // Bug
    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching categories with an invalid limit returns a 400 status code")
    public void TC1_GetCategories_With_InValidLimit()
    {
        given()
                .queryParam("limit", Variables.categoryExceedingLimit)
                .when().get("/categories")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching category by invalid ID returns a 400 status code")
    public void TC2_GetCategory_With_InvalidId()
    {
        given()
                .pathParam("id", Variables.createdCategoryId + 200)
                .when().get("/categories/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching category by invalid ID format returns a 400 status code")
    public void TC3_GetCategory_With_InvalidIdFormat()
    {
        String catId = "abc";

        given()
                .pathParam("id", catId)
                .when().get("/categories/{id}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching category by invalid slug returns 400 status code")
    public void TC4_GetCategories_With_InValidSlug()
    {
        String catSlug = "testslug";

        given()
                .pathParam("slug", catSlug)
                .when().get("/categories/slug/{slug}")
                .then()
                .assertThat()
                .statusCode(400);
    }

    // Bug
    @Severity(SeverityLevel.MINOR)
    @Description("Verify creating an already existing category fails with 400 status code")
    @Test (dataProvider = "createCategoryValidData", dataProviderClass = CategoryDataProvider.class)
    public void TC5_CreateExistentCategory(Category category)
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
    public void TC6_CreateCategory_With_InValidImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .body(category)
                .when().post("categories/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    // Bug
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify creating a category without name fails")
    @Test (dataProvider = "createCategoryWithoutName", dataProviderClass = CategoryDataProvider.class)
    public void TC7_CreateCategory_Without_Name(Category category)
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
    public void TC8_CreateCategory_Without_ImageLink(Category category)
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
    @Description("Verify updating a category with invalid image link fails")
    @Test (dataProvider = "updateExistingCategoryInValidImageLink", dataProviderClass = CategoryDataProvider.class)
    public void TC9_UpdateExistingCategory_InValidImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.categoryValidId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    // Bug
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a category with only image link fails")
    @Test (dataProvider = "updateExistingCategoryOnlyImageLink", dataProviderClass = CategoryDataProvider.class)
    public void TC10_UpdateExistingCategory_OnlyImageLink(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.createdCategoryId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    // Bug
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify updating a category with only name fails")
    @Test (dataProvider = "updateExistingCategoryOnlyName", dataProviderClass = CategoryDataProvider.class)
    public void TC11_UpdateExistingCategory_OnlyName(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.createdCategoryId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify updating a non-existent category fails with 400 status code")
    @Test (dataProvider = "updateNonExistentCategory", dataProviderClass = CategoryDataProvider.class)
    public void TC12_UpdateNonExistentCategory(Category category)
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.categoryInValidId)
                .body(category)
                .when().put("/categories/{id}")
                .then()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify deleting a non-existent category returns 200 with body 'true'")
    public void TC13_DeleteNonExistentCategory()
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.categoryInValidId)
                .when().delete("/categories/{id}")
                .then()
                .statusCode(400);
    }

    // Bug
    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching products for an invalid category ID returns 400")
    public void TC14_GetProducts_For_InValidCategoryID()
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.productInValidId)
                .when().get("/categories/{id}/products")
                .then()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify fetching products without providing a category ID returns 400")
    public void TC15_GetProducts_Without_CategoryID()
    {
        given()
                .contentType("application/json")
                .pathParam("id", "")
                .when().get("/categories/{id}/products")
                .then()
                .statusCode(400);
    }
}
