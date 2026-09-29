package tests;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Baseclass {


    Playwright playwright;
    Browser browser;
    Page page;
    String base_url;


    @BeforeMethod(alwaysRun = true)
    public void setUp() throws IOException {

        Properties prop = new Properties();
        FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
        prop.load(fis);

         // Allure command - allure serve target/allure-results
        //mvn test -Psmoke -Dbrowser=chrome
        String browserName = System.getProperty("browser") != null ? System.getProperty("browser") : prop.getProperty("browser");
        String envName = System.getProperty("env") != null ? System.getProperty("env") : prop.getProperty("env");
        System.out.println("browser name is "+browserName);
        playwright = Playwright.create();

        if ("firefox".equalsIgnoreCase(browserName)) {

            browser = playwright.firefox()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));

        } else if ("webkit".equalsIgnoreCase(browserName)) {

            browser = playwright.webkit()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));

        } else if ("chrome".equalsIgnoreCase(browserName)) {

            browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false)
                            .setChannel("chrome"));

        } else if ("chromium".equalsIgnoreCase(browserName)) {

            browser = playwright.chromium()
                    .launch(new BrowserType.LaunchOptions()
                            .setHeadless(false));

        } else {

            throw new RuntimeException(
                    "Invalid browser name: " + browserName
            );
        }


        page = browser.newPage();
        base_url = prop.getProperty(envName+".base_url");


    }

    @AfterMethod
    public void tearDown() {
        //Logout
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();

    }

}
