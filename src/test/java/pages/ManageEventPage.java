package pages;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class ManageEventPage {

    Page page;

    public ManageEventPage(Page page){

        this.page = page;

    }

    public void navigatesToManageEvents(){

        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Manage Events")).first().click();

    }

    public void waitForEventToLoad(){
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("+ New Event"))).isVisible();
    }



}
