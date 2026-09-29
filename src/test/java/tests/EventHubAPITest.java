package tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class EventHubAPITest {


    @Test(groups = {"api"})
    public void endToEndApiTesting (){

        // Login POST Call

        HashMap<Object,Object> hashMapPayload = new HashMap<>();
        hashMapPayload.put("email","rehmansabbu@gmail.com");
        hashMapPayload.put("password","Sabb28uz@9");

        Playwright playwright = Playwright.create();
        APIRequestContext request = playwright.request().newContext();
        APIResponse response= request.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(hashMapPayload));

        System.out.println(response.status());
        System.out.println(response.text());
        Assert.assertTrue(response.ok());
        // Assert.assertTrue(response.equals(200));


        // Create the Event POST call

        String token = JsonPath.read(response.text(),"$.token");
        System.out.println(token);

        Map<Object,Object> createEventData = new HashMap<>();
        createEventData.put("title","QA Event Workshop");
        createEventData.put("description","QA Submit Event form");
        createEventData.put("category","Workshop");
        createEventData.put("venue","California sector 2");
        createEventData.put("city","California");
        createEventData.put("eventDate","2026-10-30T15:32:00.000Z");
        createEventData.put("price","100");
        createEventData.put("totalSeats","300");


        APIRequestContext createEventRequest = playwright.request().newContext();
        APIResponse createEventRes= createEventRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events" ,
                RequestOptions.create().setHeader("Authorization","Bearer "+token).setData(createEventData));

        Assert.assertTrue(createEventRes.ok());
        System.out.println(createEventRes.status());
        System.out.println(createEventRes.text());

        String eventId= JsonPath.read(createEventRes.text(),"$.data.id").toString();
        System.out.println("Event is created : "+eventId);


        // Get The created Event List , GET call

        APIRequestContext apiCreatedEventList = playwright.request().newContext();

        APIResponse apiCreatedEventListRes = apiCreatedEventList
                .get("https://api.eventhub.rahulshettyacademy.com/api/events?page=1&limit=12",
                        RequestOptions.create().setHeader("Authorization", "Bearer "+token));

        Assert.assertTrue(apiCreatedEventListRes.ok());
        System.out.println(apiCreatedEventListRes.status());
        System.out.println(apiCreatedEventListRes.text());
        String listOfEvent = JsonPath.read(apiCreatedEventListRes.text(), "$.data[*].id").toString();
        System.out.println("List of IDs: "+listOfEvent);
        String listOfIDs = JsonPath.read(apiCreatedEventListRes.text(), "$.data[*].title").toString();
        System.out.println("List of Titles: "+listOfIDs);



        // Delete the Event > Delete call

        APIRequestContext eventDeleteContext = playwright.request().newContext();
        APIResponse resEventDelete = eventDeleteContext.
                delete("https://api.eventhub.rahulshettyacademy.com/api/events/6853",
                        RequestOptions.create().setHeader("Authorization", "Bearer "+token));

        Assert.assertFalse(resEventDelete.equals("404 Not Found"));
        System.out.println(resEventDelete.status());
        System.out.println(resEventDelete.text());
        System.out.println(resEventDelete.headers());











    }




}
