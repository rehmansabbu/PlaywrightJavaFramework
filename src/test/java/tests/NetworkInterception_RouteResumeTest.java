package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NetworkInterception_RouteResumeTest {

    Playwright playwright;
    Browser browser;
    Page page;


    @BeforeMethod
    public void setUp() {

        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/login");

    }

    @Test(description = "Login & Navigates to My Bookings Page >> Check and controlled UI by using Route " +
            "Resume method")
    public void routeFullfillTest() {


        page.getByPlaceholder("you@email.com").fill("rehmansabbu@gmail.com");
        page.getByLabel("Password").fill("Sabb28uz@9");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →")))
                .isVisible();

        page.waitForTimeout(3000);

        // Before performing action Click for My Bookings we will use route resume for fake data in UI

        page.route("**/api/bookings**", route -> route.resume(new Route.ResumeOptions()
                .setUrl("https://eventhub.rahulshettyacademy.com/bookings/11960"))
        );


        // Navigates to My Bookings and Verify the fake page link
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("My Bookings")).first().click();
        page.waitForTimeout(7000);






    }



}
