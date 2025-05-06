package utils;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.io.File;

public class TestListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite)
    {
        File allureFolder = new File("test-outputs/allure-results");

        if (allureFolder.exists())
        {
            for (File file : allureFolder.listFiles())
            {
                file.delete();
            }
            allureFolder.delete();
        }
    }

    @Override
    public void onFinish(ISuite suite)
    {
        AllureUtils.generateAllureReport();
    }
}
