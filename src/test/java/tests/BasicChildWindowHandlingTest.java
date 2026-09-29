package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class BasicChildWindowHandlingTest {
    Playwright playwright;
    Browser  browser;
    Page pageA;     Page pageB;

    @BeforeMethod (alwaysRun = true)
    public void setUp(){
        playwright = Playwright.create();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));


    }

    @Test(groups = {"smoke"}, description = "Ui Window and child window handling using context " )
    public void windowHandling(){

        BrowserContext context = browser.newContext();
        pageA = context.newPage();
        pageA.navigate("https://rahulshettyacademy.com/loginpagePractise/");
        String title = pageA.title();
        System.out.println(title);
        pageA.waitForTimeout(2000);




        pageB = context.waitForPage(()-> {
                    pageA.getByRole(AriaRole.LINK, new Page.GetByRoleOptions()
                            .setName("Free Access to InterviewQues/ResumeAssistance/Material")).click();
                });

        pageB.waitForLoadState();
        System.out.println("Child Title: " + pageB.title());
        String name = pageB.getByText("Learning Paths").first().innerText(new Locator.InnerTextOptions().setTimeout(10000));
        System.out.println("Extracted Text: " + name);

        String emailCompleteText =pageB.getByText("Please email us at mentor@rahulshettyacademy.com with below template to receive response").textContent();
        System.out.println(emailCompleteText);
        //Please email us at mentor@rahulshettyacademy.com with below template to receive response

        String [] spWord = emailCompleteText.split(" ");
        String text = spWord[4];
        String finalText = text.split("@")[1];
        System.out.println(finalText);


        // Back to main window Page
        pageA.locator("#username").fill(finalText);
        pageA.pause();


        context.close();




    }

    @AfterMethod
    public void tearDown(){

        browser.close();
    }


}
