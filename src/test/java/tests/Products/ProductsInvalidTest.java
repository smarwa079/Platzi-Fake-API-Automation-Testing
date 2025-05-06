package tests.Products;

import DataProviders.ProductDataProvider;
import POJO.Product;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestBase;
import utils.Variables;

import static io.restassured.RestAssured.given;

public class ProductsInvalidTest extends TestBase
{
    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Check API behavior when requesting a product ID that does not exist.")
    public void TC1_GetProduct_By_InValidId()
    {
        Response response = given()
                .pathParam("id", Variables.productInValidId)
                .when().get("/products/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Test
    @Severity (SeverityLevel.MINOR)
    @Description ("Check API behavior when requesting a product Slug that does not exist.")
    public void TC2_GetProduct_By_InValidSlug()
    {
        String productSlug = "trendy-pink";

        Response response = given()
                .pathParam("slug", productSlug)
                .when().get("/products/slug/{slug}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify creating an already existing product fails with 400 status code")
    @Test (dataProvider = "createProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC3_CreateExistentProduct(Product product)
    {
        given()
                .body(product)
                .header("Content-Type", "application/json")
                .when().post("/products/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Verify that an error is returned when required fields are missing.")
    @Test (dataProvider = "createProductMissingCategory", dataProviderClass = ProductDataProvider.class)
    public void TC4_CreateProduct_With_MissingCategory(Product product) {

        given()
                .body(product)
                .header("Content-Type", "application/json")
                .when().post("/products/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    // Bug
    @Severity (SeverityLevel.MINOR)
    @Description ("Verify that the API returns an error when attempting to update a product using the unsupported PATCH method.")
    @Test (dataProvider = "updateExistingProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC5_UpdateExistingProduct_Using_PATCH(Product product)
    {
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdProductId)
                .body(product)
                .when().patch("/products/{id}")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Severity (SeverityLevel.MINOR)
    @Description ("Check if the API returns an appropriate error when updating a product that doesn't exist.")
    @Test (dataProvider = "updateNonExistentProductData", dataProviderClass = ProductDataProvider.class)
    public void TC6_UpdateNonExistentProduct(Product product)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.productInValidId)
                .body(product)
                .when().put("/products/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the API response when attempting to delete a product that does not exist.")
    public void TC7_DeleteNonExistentProduct()
    {
        Response response = given()
                .pathParam("id", Variables.productInValidId)
                .when().delete("/products/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertNotEquals(response.getBody().toString(), "true");
    }
}
