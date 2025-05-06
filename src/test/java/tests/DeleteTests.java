package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import utils.TestBase;
import utils.Variables;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteTests extends TestBase
{
    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify successfully deleting an existing category")
    public void TC1_DeleteExistingCategory()
    {
        given()
                .contentType("application/json")
                .pathParam("id", Variables.createdCategoryId)
                .when().delete("/categories/{id}")
                .then()
                .body(equalTo("true"))
                .statusCode(200);
    }

    @Test
    @Severity (SeverityLevel.CRITICAL)
    @Description ("Ensure that a product can be successfully deleted.")
    public void TC2_DeleteExistingProduct()
    {
        given()
                .pathParam("id", Variables.createdProductId)
                .when().delete("/products/{id}")
                .then()
                .assertThat()
                .statusCode(200)
                .body(equalTo("true"));
    }
}
