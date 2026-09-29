package pages;

import com.microsoft.playwright.Page;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BookingFormPage {

    Page page;
    String placeHolderBy_Name="Your full name";
    String label_Email="Email";
    String label_PhoneNumber="Phone Number";
    String locator_Submit="[type='submit']";


    public BookingFormPage(Page page){

        this.page= page;

    }


    public void fillBookingAndConfirm(String name,String email,String phoneNumber){

        // Fill the Booking
        page.getByPlaceholder(placeHolderBy_Name).fill("sabbu");
        page.getByLabel(label_Email).fill("rehmansabbu@gmail.com");
        page.getByLabel(label_PhoneNumber).fill("8976549876");
        //page.locator("[type='button']").nth(1).click();
        page.locator(locator_Submit).click();

        // verify the booking confirmation fetch the Booking Ref
        assertThat(page.getByText("Your tickets are reserved.")).isVisible();

    }



}
