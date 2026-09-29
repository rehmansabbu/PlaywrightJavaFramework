package pages;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.options.AriaRole;
import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class EventsPage {

    Page page;

    public EventsPage(Page page){

        this.page= page;

    }

    public void goToEventPage(){

        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Events")).first().click();

    }

    public Locator waitForallEventCardToLoad(){

        Locator allEventCard = page.getByTestId("event-card");
        assertThat(page.getByTestId("event-card").first()).isVisible();
        return allEventCard;
    }

    public Locator findEventCard(String titleOfCard){
        Locator allEventCard = waitForallEventCardToLoad();
        Locator targetEventCard = allEventCard.filter(new Locator.FilterOptions().setHasText(titleOfCard));
        assertThat(targetEventCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000));
        return targetEventCard;
    }

    public int seatCountBeforeBooking(String titleOfCard){

        Locator targetEventCard= findEventCard(titleOfCard);
        String beforeBookingSeatsAvailable = targetEventCard.getByText("seats").innerText();
        System.out.println(beforeBookingSeatsAvailable);

        // Split and fetch the Number
        String [] spWord = beforeBookingSeatsAvailable.split(" ");
        String beforeBookingSeatNumberStr= spWord[0];
        int beforeBookingSeatNumber = Integer.parseInt(beforeBookingSeatNumberStr);
        return beforeBookingSeatNumber;

    }

    public BookingFormPage proceedToBookingEvent(String titleOfCard){

        // Book Now
        Locator targetEventCard= findEventCard(titleOfCard);
        targetEventCard.getByText("Book Now").click();
        Locator bookTicket = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Book Tickets"));
        assertThat(bookTicket).isVisible();
        return new BookingFormPage(page);
    }




}
