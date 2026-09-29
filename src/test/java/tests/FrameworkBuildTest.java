package tests;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.*;
import utilis.DataProviderTest;
import java.io.IOException;
import java.util.HashMap;



public class FrameworkBuildTest extends Baseclass {

   @DataProvider(name="eventBookingData")
   public Object [][] eventBookingData() throws IOException {

       return  DataProviderTest.getJsonDataToMap("/src/main/resources/DataProviderFile.json");

   }

    @Test(dataProvider = "eventBookingData", groups = {"framework"} ,description = "Create Event -> Book the event and verify if its booked")
    public void demoTest(HashMap<String,String> data) {

        LoginPage loginPage = new LoginPage(page ,base_url);
        loginPage.loginToApplication();

        ManageEventPage manageEventPage = new ManageEventPage(page);
        manageEventPage.navigatesToManageEvents();
        manageEventPage.waitForEventToLoad();

        // Fill the form for creating new Event
        CreatingNewEventPage creatingNewEventPage = new CreatingNewEventPage(page);
        creatingNewEventPage.createNewEvent
                (data.get("title"),
                        (data.get("city")),
                        (data.get("address")),
                        (data.get("datetime")),
                        (data.get("price")),
                        (data.get("totalseat")));

        EventsPage eventsPage = new EventsPage(page);
        eventsPage.goToEventPage();
        eventsPage.findEventCard((data.get("title")));
        int seatNumberBeforeBooking = eventsPage.seatCountBeforeBooking((data.get("title")));

        BookingFormPage bookingFormPage =eventsPage.proceedToBookingEvent(data.get("title"));
        bookingFormPage.fillBookingAndConfirm
                (data.get("name"),data.get("email"),data.get("phone"));




//        //#Booking Ref
//        String afterBookingRefNumber = page.locator(".booking-ref").innerText();
//        System.out.println(afterBookingRefNumber);
//
//
//        // Visiblity of After Booking
//        page.locator("#nav-bookings").click();
//        Locator afterBooking = page.locator("#booking-card");
//        assertThat(afterBooking.filter(new Locator.FilterOptions().setHasText(afterBookingRefNumber))).isVisible();
//        afterBooking.getByText("View Details").first().click();
//
//
////       // Deducted Seats Available
//        page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Events")).first().click();
//        page.waitForTimeout(5000);
//        Locator afterBookingEventCard = page.getByTestId("event-card");
//        Locator targetEventCardAfterBooking = afterBookingEventCard.filter(new Locator.FilterOptions().setHasText("FrameWork Test"));
//
//        String afterBookingDeductedseatsAvailable =targetEventCardAfterBooking.getByText("seats").innerText();
//        System.out.println(afterBookingDeductedseatsAvailable);
//
//        // Split & Compare
//        String [] spWrd = afterBookingDeductedseatsAvailable.split(" ");
//        String deductedSeatNumberStr= spWrd[0];
//        int deductedSeatNumber = Integer.parseInt(deductedSeatNumberStr);
//        System.out.println(deductedSeatNumber);


       // Assert.assertTrue(beforeBookingSeatNumber > deductedSeatNumber ,"Final Test result is pass");



    }




}
