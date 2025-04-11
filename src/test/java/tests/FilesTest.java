package tests;

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

public class FilesTest extends TestBase {

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

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if the server handles missing file uploads and returns the correct error.")
    public void TC2_UploadWithMissingFile()
    {
        given()
                .when().post("/files/upload")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the server rejects unsupported file types and returns an error.")
    public void TC3_UploadFile_InvalidFileType() {

        File invalidFile = new File("Files-To-Upload/chromedriver.exe");

        given()
                .multiPart("file", invalidFile)
                .when().post("/files/upload")
                .then()
                .assertThat()
                .statusCode(415);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the server handles incorrect content type headers and returns an error.")
    public void TC4_UploadFile_With_IncorrectContentType() {

        File file = new File("Files-To-Upload/skincare.jpg");

        given()
                .header("Content-Type", "application/json")
                .multiPart("file", file)
                .when().post("/files/upload")
                .then()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Ensures uploaded files can be retrieved by filename.")
    public void TC5_GetFile_Successfully() {

        Response response = given()
                .pathParam("fileName", uploadedFileName)
                .when().get("/files/{fileName}")
                .then()
                .statusCode(200)
                .extract().response();

        Assert.assertEquals(response.getHeader("Content-Type"), "image/png");
    }

    @Test
    @Severity(SeverityLevel.MINOR)
    @Description("Verifies that the server returns an error when trying to retrieve a non-existent file.")
    public void TC6_GetNonExistentFile()
    {
        String filename = "nonexistentfile.png";

        given()
                .pathParam("fileName", filename)
                .when().get("/files/{fileName}")
                .then()
                .statusCode(400);
    }
}
