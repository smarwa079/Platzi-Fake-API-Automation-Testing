package tests.Files;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.testng.annotations.Test;
import utils.TestBase;

import java.io.File;

import static io.restassured.RestAssured.given;

public class FilesInvalidTest extends TestBase
{
    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifies that uploading an already existing file returns the correct error response.")
    public void TC1_UploadExistingFile()
    {
        File file = new File("Files-To-Upload/makeup.jpg");

        given()
                .multiPart("file",file)
                .when().post("/files/upload")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Checks if the server handles missing file uploads and returns the correct error.")
    public void TC2_UploadWithMissingFile()
    {
        given()
                .multiPart("file", "")
                .when().post("/files/upload")
                .then()
                .assertThat()
                .statusCode(400);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifies that the server rejects unsupported file types and returns an error.")
    public void TC3_UploadFile_InvalidFileType() {

        File invalidFile = new File("Files-To-Upload/Freelancing_Basics.pdf");

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
    @Severity(SeverityLevel.MINOR)
    @Description("Verifies that the server returns an error when trying to retrieve a non-existent file.")
    public void TC5_GetNonExistentFile()
    {
        String filename = "nonexistentfile.png";

        given()
                .pathParam("fileName", filename)
                .when().get("/files/{fileName}")
                .then()
                .statusCode(404);
    }
}
