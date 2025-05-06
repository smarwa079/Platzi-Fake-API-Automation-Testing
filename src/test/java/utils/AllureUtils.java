package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllureUtils
{
    private static final String ALLURE_REPORT_PATH = "reports/allure-report";
    private static final String ALLURE_RESULTS_PATH = "test-outputs/allure-results";

    private static final Logger log = LoggerFactory.getLogger(AllureUtils.class);

    public static void generateAllureReport()
    {
        try {
            String command = "allure generate "+ALLURE_RESULTS_PATH+" --clean -o "+ALLURE_REPORT_PATH+" --single-file";

            ProcessBuilder builder = new ProcessBuilder();
            // Windows
            builder.command("cmd.exe", "/c", command);

            builder.inheritIO();

            Process process = builder.start();
            process.waitFor();

            log.info("Allure report generated successfully");
        } catch (Exception e) {
            log.error("Error while generating Allure report", e);
        }
    }
}
