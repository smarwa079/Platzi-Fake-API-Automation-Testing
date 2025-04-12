package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import POJO.Product;
import DataProviders.ProductDataProvider;
import utils.TestBase;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

public class ProductsTest extends TestBase {

    int createdProductId;
    int productValidId;
    String productValidSlug;

    @Test (priority = 1)
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

        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "List should not be empty");

        productValidId = response.jsonPath().getInt("[0].id");
        productValidSlug = response.jsonPath().getString("[0].slug");
    }

    @Severity(SeverityLevel.NORMAL)
    @Test (dependsOnMethods = "TC1_GetAllProducts")
    @Description ("Ensure the API returns the correct product details when a valid product ID is provided.")
    public void TC2_GetProduct_By_ValidId()
    {
        Response response = given()
                .pathParam("id", productValidId)
                .when().get("/products/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getInt("id"), productValidId);
    }

    @Test
    @Severity (SeverityLevel.MINOR)
    @Description ("Check API behavior when requesting a product ID that does not exist.")
    public void TC3_GetProduct_By_InValidId()
    {
        int productId = -1;

        Response  response = given()
                .pathParam("id", productId)
                .when().get("/products/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Severity (SeverityLevel.NORMAL)
    @Test (dependsOnMethods = "TC1_GetAllProducts")
    @Description ("Ensure the API returns the correct product details when a valid product Slug is provided.")
    public void TC4_GetProduct_By_ValidSlug()
    {
        Response response = given()
                .pathParam("slug", productValidSlug)
                .when().get("/products/slug/{slug}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .statusCode(200)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("slug"), productValidSlug);
    }

    @Test
    @Severity (SeverityLevel.MINOR)
    @Description ("Check API behavior when requesting a product Slug that does not exist.")
    public void TC5_GetProduct_By_InValidSlug()
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

    @Severity(SeverityLevel.CRITICAL)
    @Description ("Validate that a new product can be created with valid data.")
    @Test (priority = 2, dataProvider = "createProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC6_CreateProduct_With_ValidData(Product product) {

        Response response = given()
                .body(product)
                .header("Content-Type", "application/json")
                .when().post("/products/")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .extract()
                .response();
        createdProductId = response.jsonPath().getInt("id");
        Assert.assertEquals(response.statusCode(), 201, "Status code should be 201");
    }

    @Severity(SeverityLevel.MINOR)
    @Description("Verify creating an already existing product fails with 400 status code")
    @Test (dependsOnMethods = "TC6_CreateProduct_With_ValidData", dataProvider = "createProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC7_CreateExistentProduct(Product product)
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
    @Test (dataProvider = "createProductInValidData", dataProviderClass = ProductDataProvider.class)
    public void TC8_CreateProduct_With_InValidData(Product product) {

        given()
                .body(product)
                .header("Content-Type", "application/json")
                .when().post("/products/")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Severity (SeverityLevel.CRITICAL)
    @Description ("Confirm that updating a product with valid data modifies the existing product.")
    @Test (priority = 3, dataProvider = "updateExistingProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC9_UpdateExistingProduct(Product product)
    {
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", createdProductId)
                .body(product)
                .when().put("/products/{id}")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("product-schema.json"))
                .statusCode(200);
    }

    @Severity (SeverityLevel.MINOR)
    @Description ("Verify that the API returns an error when attempting to update a product using the unsupported PATCH method.")
    @Test (dependsOnMethods = "TC6_CreateProduct_With_ValidData", dataProvider = "updateExistingProductValidData", dataProviderClass = ProductDataProvider.class)
    public void TC10_UpdateExistingProduct_Using_PATCH(Product product)
    {
        given()
                .header("Content-Type", "application/json")
                .pathParam("id", createdProductId)
                .body(product)
                .when().patch("/products/{id}")
                .then()
                .assertThat()
                .statusCode(405);
    }

    @Severity (SeverityLevel.MINOR)
    @Description ("Check if the API returns an appropriate error when updating a product that doesn't exist.")
    @Test (dataProvider = "updateNonExistentProductData", dataProviderClass = ProductDataProvider.class)
    public void TC11_UpdateNonExistentProduct(Product product)
    {
        Response response = given()
                .header("Content-Type", "application/json")
                .pathParam("id", createdProductId-1)
                .body(product)
                .when().put("/products/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("name"), "EntityNotFoundError");
    }

    @Test (priority = 4)
    @Severity (SeverityLevel.CRITICAL)
    @Description ("Ensure that a product can be successfully deleted.")
    public void TC12_DeleteExistingProduct()
    {
        given()
                .pathParam("id", createdProductId)
                .when().delete("/products/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("true"));
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verify the API response when attempting to delete a product that does not exist.")
    public void TC13_DeleteNonExistentProduct() {

        int productId = 1;

        Response response = given()
                .pathParam("id", productId)
                .when().delete("/products/{id}")
                .then()
                .assertThat()
                .statusCode(400)
                .extract()
                .response();

        Assert.assertNotEquals(response.getBody().toString(), "true");
    }
}
