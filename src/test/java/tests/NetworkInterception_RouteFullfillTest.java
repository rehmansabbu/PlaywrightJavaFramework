package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class NetworkInterception_RouteFullfillTest {

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

    @Test(description = "Login & Navigates to Event Page >> Check and controlled UI by using Route " +
            "Fullfill method")
    public void routeFullfillTest() {


        page.getByPlaceholder("you@email.com").fill("rehmansabbu@gmail.com");
        page.getByLabel("Password").fill("Sabb28uz@9");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →")))
                .isVisible();

        page.waitForTimeout(3000);

        // Before performing action Click for Events we will use route fullfill for fake data in UI

        page.route("**/api/events**", route -> route.fulfill(new Route.FulfillOptions()
                .setPath(Paths.get("src/main/resources/events.json"))
        ));

        // src/main/resources/events.json
        // Navigates to Event and Verify Event is created or not
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Events")).first().click();
        page.waitForTimeout(7000);

        Locator eventCardCount= page.getByTestId("event-card");
        assertThat(eventCardCount).hasCount(3);





    }



}
