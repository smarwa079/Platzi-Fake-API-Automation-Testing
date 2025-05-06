package tests.Files;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.TestBase;

import java.io.File;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class FilesValidTest extends TestBase {

    String uploadedFileName;

    @Test
    @Severity (SeverityLevel.BLOCKER)
    @Description("Verifies that file upload works correctly and returns file information.")
    public void TC1_UploadFileSuccessfully()
    {
        File file = new File("Files-To-Upload/makeup.jpg");

        Response response= given()
                .multiPart("file",file)
                .when().post("/files/upload")
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath("file-schema.json"))
                .statusCode(201)
                .extract()
                .response();

        Assert.assertEquals(response.jsonPath().getString("originalname"), "makeup.jpg");
        uploadedFileName = response.jsonPath().getString("filename");
    }

    @Severity(SeverityLevel.BLOCKER)
    @Test (dependsOnMethods = "TC1_UploadFileSuccessfully")
    @Description("Ensures uploaded files can be retrieved by filename.")
    public void TC2_GetFile_Successfully() {

        Response response = given()
                .pathParam("fileName", uploadedFileName)
                .when().get("/files/{fileName}")
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertEquals(response.getHeader("Content-Type"), "image/jpeg");
    }
}
