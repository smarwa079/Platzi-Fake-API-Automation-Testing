package tests.Products;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import POJO.Product;
import DataProviders.ProductDataProvider;
import org.testng.asserts.SoftAssert;
import utils.TestBase;
import utils.Variables;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class ProductsValidTest extends TestBase
{
    SoftAssert softAssert = new SoftAssert();

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description ("Verify that the API returns a list of all available products.")
    public void TC1_GetAllProducts()
    {
        Response response = given()
                .when().get("/products")
                .then()
                .assertThat()
                .statusCode(200)
                .extract().response();

        List<HashMap<String, Object>> products = response.jsonPath().getList("");

        Assert.assertFalse(products.isEmpty(), "List should not be empty");

        Variables.productValidId = (int) products.getFirst().get("id");
        Variables.productValidSlug = products.getFirst().get("slug").toString();
    }

    @Severity(SeverityLevel.NORMAL)
    @Test (dependsOnMethods = "TC1_GetAllProducts")
    @Description ("Ensure the API returns the correct product details when a valid product ID is provided.")
    public void TC2_GetProduct_By_ValidId()
    {
        Response response = given()
                .pathParam("id", Variables.productValidId)
                .when().get("/products/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getInt("id"), Variables.productValidId);
    }

    @Severity (SeverityLevel.NORMAL)
    @Test (dependsOnMethods = "TC1_GetAllProducts")
    @Description ("Ensure the API returns the correct product details when a valid product Slug is provided.")
    public void TC3_GetProduct_By_ValidSlug()
    {
        Response response = given()
                .pathParam("slug", Variables.productValidSlug)
                .when().get("/products/slug/{slug}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("slug"), Variables.productValidSlug);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Validate that a new product can be created with valid data.")
    @Test (dataProvider = "createProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC4_CreateProduct_With_ValidData(Product product)
    {
        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .when().post("/products/")
                .then()
                .assertThat().log().all()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .extract()
                .response();

        HashMap<String, Object> createdProduct = response.jsonPath().get("");

        HashMap<String, Object> category = (HashMap<String, Object>) createdProduct.get("category");

        Variables.createdProductId = (int) createdProduct.get("id");
        softAssert.assertEquals(response.statusCode(), 201, "Status code should be 201");
        softAssert.assertEquals(createdProduct.get("title"), product.getTitle(), "Product name should match");
        softAssert.assertEquals(createdProduct.get("price"), product.getPrice(), "Product price should match");
        softAssert.assertEquals(createdProduct.get("description"), product.getDescription(), "Product description should match");
        softAssert.assertEquals(category.get("id"), product.getCategoryId(), "Product category should match");

        softAssert.assertAll();
    }

    // Bug
    @Severity (SeverityLevel.CRITICAL)
    @Description ("Confirm that updating a product with valid data modifies the existing product.")
    @Test (dependsOnMethods = "TC4_CreateProduct_With_ValidData", dataProvider = "updateExistingProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC5_UpdateExistingProduct_Successfully(Product product)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", Variables.createdProductId)
                .body(product)
                .when().put("/products/{id}")
                .then()
                .assertThat().log().all()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .statusCode(200)
                .extract()
                .response();
    }
}
