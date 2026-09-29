package pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class CreatingNewEventPage {

    Page page;
    String testIdBy_EventTitleInput="event-title-input";
    String locator_Category="#category";
    String label_City="City";
    String placeholder_VenueAddress="Venue name & address";
    String label_EventDate="Event Date & Time";
    String label_Price ="Price ($)";
    String label_TotalSeat ="Total Seats";
    String success_EventCreated ="Event created!";


    public CreatingNewEventPage(Page page){
        this.page = page;
    }

    public void createNewEvent (String title,String city, String address ,
                                String dateTime, String price,String totalSeat){
        // Fill the form for creating new Event
        page.getByTestId(testIdBy_EventTitleInput).fill(title);
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Describe the event…")).fill("QA Submit Event form", new Locator.FillOptions().setTimeout(5000));
        page.locator(locator_Category).selectOption("Workshop");
        page.getByLabel(label_City).fill(city);
        page.getByPlaceholder(placeholder_VenueAddress).fill(address);
        page.getByLabel(label_EventDate).fill(dateTime ); //"2026-10-30T15:32"
        page.getByLabel(label_Price).fill(price);
        page.getByLabel(label_TotalSeat).fill(totalSeat);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click(new Locator.ClickOptions().setTimeout(5000));
        assertThat(page.getByText(success_EventCreated)).isVisible();
    }
}
