package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class Practice {

    @Test (description = "UI => Testing for practice")
    public void DemoTest() {
        Playwright playwright = Playwright.create();
        Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        BrowserContext context = browser.newContext();

        Page pageA = context.newPage();
        pageA.navigate("https://rahulshettyacademy.com/loginpagePractise/");
        String title = pageA.title();
        System.out.println(title);
        pageA.waitForTimeout(3000);

        Page pageB = context.waitForPage(()->{
            pageA.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                    .setName("Free Access to InterviewQues/ResumeAssistance/Material")).click();

        });
        pageB.waitForLoadState();
        String childWindowTitle = pageB.title();


        String emailCompleteText =pageB.getByText("Please email us at mentor@rahulshettyacademy.com with below template to receive response").textContent();
        System.out.println(emailCompleteText);

        context.close();
        browser.close();





    }
}
