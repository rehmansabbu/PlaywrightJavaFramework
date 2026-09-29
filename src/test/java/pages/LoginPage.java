package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

//Note : Login Page should be independent , we should not inherit

public class LoginPage  {
    Page page;
    String base_url;
    private static final String email_Placeholder = "you@email.com";
    private static final String password_Label = "Password";

   public LoginPage(Page page , String baseUrl){
       this.page = page;
       this.base_url = baseUrl;

    }

    public void loginToApplication(){
        page.navigate(base_url);
        String title = page.title();
        System.out.println(title);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder(email_Placeholder).fill("rehmansabbu@gmail.com");
        page.getByLabel(password_Label).fill("Sabb28uz@9");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →")))
                .isVisible();

    }


}
