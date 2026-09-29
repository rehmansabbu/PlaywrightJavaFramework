package tests;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;


public class BasicTest {

    Playwright playwright;
    Browser browser;
    Page page;


    @BeforeMethod(alwaysRun = true)
    public void setUp() {

        playwright = Playwright.create();
        // playwright page level wait by default is 30 second & for assertion is 5 second , if application is slow then Tester can customize the Wait.


        // Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));
        // Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //For Local Browser
        // Browser browser =playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));


        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
        page = browser.newPage();
        page.setDefaultTimeout(8000); // 8 second
        page.navigate("https://eventhub.rahulshettyacademy.com/login");
        PlaywrightAssertions.setDefaultAssertionTimeout(7000); // Global assertion timeout 7 sec

    }


    @Test(groups = {"smoke"}, description = "Create Event -> Book the event and verify if its booked")
    public void demoTest() {

        String title = page.title();
        System.out.println(title);
        assertThat(page).hasTitle("EventHub — Discover & Book Events");
        page.getByPlaceholder("you@email.com").fill("rehmansabbu@gmail.com");
        page.getByLabel("Password").fill("Sabb28uz@9");
        // page.locator("#login-btn").click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →")))
                .isVisible();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Admin")).click();
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Manage Events")).first().click();
        assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("+ New Event"))).isVisible();

        // Fill the form for creating new Event
        page.getByTestId("event-title-input").fill("QA Submit Event");
        page.getByRole(AriaRole.TEXTBOX, new Page.GetByRoleOptions().setName("Describe the event…")).fill("QA Submit Event form", new Locator.FillOptions().setTimeout(5000));
        page.locator("#category").selectOption("Workshop");
        page.getByLabel("City").fill("London");
        page.getByPlaceholder("Venue name & address").fill("California");
        page.getByLabel("Event Date & Time").fill("2026-10-30T15:32");
        page.getByLabel("Price ($)").fill("200");
        page.getByLabel("Total Seats").fill("250");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("+ Add Event")).click(new Locator.ClickOptions().setTimeout(5000));
        assertThat(page.getByText("Event created!")).isVisible();

        // Navigates to Event and Verify Event is created or not
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Events")).first().click();
        // assertThat(page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("QA Submit Event"))).isVisible();
        // Other Way
        Locator allEventCard = page.getByTestId("event-card");
        System.out.println(allEventCard.count());

        // Visblity of card which we have added
        Locator targetEventCard = allEventCard.filter(new Locator.FilterOptions().setHasText("QA Submit Event"));
        assertThat(targetEventCard).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(10000)); // 10 second

        String beforeBookingSeatsAvailable = targetEventCard.getByText("seats").innerText();
        System.out.println(beforeBookingSeatsAvailable);

        // Split and fetch the Number
        String [] spWord = beforeBookingSeatsAvailable.split(" ");
        String beforeBookingSeatNumberStr= spWord[0];
        int beforeBookingSeatNumber = Integer.parseInt(beforeBookingSeatNumberStr);
        System.out.println(beforeBookingSeatNumber);

        // Book Now
        targetEventCard.getByText("Book Now").click(new Locator.ClickOptions().setTimeout(10000));

        // Navigates to Book Now page
        Locator bookTicket = page.getByRole(AriaRole.HEADING, new Page.GetByRoleOptions().setName("Book Tickets"));
        assertThat(bookTicket).isVisible(new LocatorAssertions.IsVisibleOptions().setTimeout(9000));

        // Fill the Booking
        page.getByPlaceholder("Your full name").fill("sabbu");
        page.getByLabel("Email").fill("rehmansabbu@gmail.com");
        page.getByLabel("Phone Number").fill("8976549876");
        //page.locator("[type='button']").nth(1).click();
        page.locator("[type='submit']").click();

        // verify the booking confirmation fetch the Booking Ref
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();
        //#Booking Ref
        String afterBookingRefNumber = page.locator(".booking-ref").innerText();
        System.out.println(afterBookingRefNumber);


        // Visiblity of After Booking
        page.locator("#nav-bookings").click();
        Locator afterBooking = page.locator("#booking-card");
        assertThat(afterBooking.filter(new Locator.FilterOptions().setHasText(afterBookingRefNumber))).isVisible();
        afterBooking.getByText("View Details").first().click();


//       // Deducted Seats Available
        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Events")).first().click();
        page.waitForTimeout(5000);
        Locator afterBookingEventCard = page.getByTestId("event-card");
        Locator targetEventCardAfterBooking = afterBookingEventCard.filter(new Locator.FilterOptions().setHasText("QA Submit Event"));

        String afterBookingDeductedseatsAvailable =targetEventCardAfterBooking.getByText("seats").innerText();
        System.out.println(afterBookingDeductedseatsAvailable);

        // Split & Compare
        String [] spWrd = afterBookingDeductedseatsAvailable.split(" ");
        String deductedSeatNumberStr= spWrd[0];
        int deductedSeatNumber = Integer.parseInt(deductedSeatNumberStr);
        System.out.println(deductedSeatNumber);


        Assert.assertTrue(beforeBookingSeatNumber > deductedSeatNumber ,"Final Test result is pass");


       // page.pause();


    }

    @AfterMethod
    public void tearDown() {
        //Logout
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Logout")).click();

    }


}
