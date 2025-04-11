package utils;

import io.restassured.RestAssured;
import org.testng.annotations.BeforeSuite;

public class TestBase {

    @BeforeSuite
    public void setup()
    {
        RestAssured.baseURI = "https://api.escuelajs.co/api/v1";
    }
}
