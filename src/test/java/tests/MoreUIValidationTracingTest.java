package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.nio.file.Paths;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MoreUIValidationTracingTest {
    Page page;
    Playwright playwright;
    Browser browser;
    BrowserContext context;

    @BeforeMethod(alwaysRun = true)
    public void setUp(){

        // page.setDefaultTimeout(10000);

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        context = browser.newContext();

        // Start tracing before creating / navigating a page.
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));

        page= context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");


    }

    @Test(description = "UI Validation")
    public void UIValidationTest(){
        String title = page.title();
        System.out.println(title);
        assertThat(page).hasTitle(title);

        // Login with Incorrect username and password
        page.locator("#username").fill("rahulshettyacademy1");
        page.locator("#password").fill("Learning@830$3mK23");
        page.locator("#signInBtn").click(new Locator.ClickOptions().setTimeout(5000));
        Locator incorrectCredentials = page.locator("div.alert-danger");
        assertThat(incorrectCredentials).isVisible();

        // Login with Correct username and password

        // Login with Incorrect username and password
        page.locator("#username").fill("rahulshettyacademy");
        page.locator("#password").fill("Learning@830$3mK2");
        page.locator("select.form-control").selectOption("Teacher");
        page.locator("#signInBtn").click();

        // Verify navigates to Shop Page
        assertThat(page.getByRole(AriaRole.HEADING,new Page.GetByRoleOptions().setName("Samsung Note 8"))).isVisible();

    }
    @Test(description = "Validate on Radio Button , CheckBox and Drop Down")
    public void moreValidation(){

        Locator clickOnUserRadioButton=page.getByRole(AriaRole.RADIO,new Page.GetByRoleOptions().setName(" User"));
        clickOnUserRadioButton.click();
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Okay")).click();
        assertThat(clickOnUserRadioButton).isChecked();


        Locator ClickOnTermsCheckbox=page.getByRole(AriaRole.CHECKBOX,new Page.GetByRoleOptions()
                .setName("I Agree to the terms and conditions"));
        ClickOnTermsCheckbox.click();
        Assert.assertTrue(ClickOnTermsCheckbox.isChecked());

        page.locator("select.form-control").selectOption("Teacher");
        page.waitForTimeout(7000);

    }


    @AfterMethod
    public void tearDown(){

        // Stop tracing and export it into a zip archive.
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));

    }


}
