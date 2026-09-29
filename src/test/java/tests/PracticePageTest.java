package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class PracticePageTest {
    Page page;

    @BeforeMethod(alwaysRun = true)
    public void setUp(){

        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        BrowserContext context = browser.newContext();
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/AutomationPractice/");
    }


    @Test(groups = {"smoke"})
    public void testAlertPopupPracticePage(){

        String title = page.title();
        System.out.println(title);
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();
        page.locator("#hide-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isHidden();
        page.locator("#show-textbox").click();
        assertThat(page.getByPlaceholder("Hide/Show Example")).isVisible();

        // Handling Alert Popup

        page.onceDialog(dialog -> dialog.accept());
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Alert")).click();
        page.waitForTimeout(1000);

        // Mouse hover
        page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Mouse Hover")).hover();
        page.waitForTimeout(5000);
        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Top")).click();
//        page.waitForTimeout(3000);
//        page.getByRole(AriaRole.LINK,new Page.GetByRoleOptions().setName("Reload")).click();

        // Frame Handling
        FrameLocator framePage =page.frameLocator("#courses-iframe");
        framePage.getByRole(AriaRole.LINK, new FrameLocator.GetByRoleOptions().setName("Courses")).first().click();
        page.waitForTimeout(5000);
    }
    @Test(description = "Screenshort Testing")
    public void screenshot(){

        // full Page screenshort
        page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("FullPageScreenshot.png")));
        // Locator level screenshot
        Locator locatorLevel = page.getByRole(AriaRole.BUTTON,new Page.GetByRoleOptions().setName("Mouse Hover"));
        locatorLevel.screenshot(new Locator.ScreenshotOptions().setPath(Paths.get("loctor.png")));

    }


    @AfterMethod
    public void tearDown(){

    }





}
