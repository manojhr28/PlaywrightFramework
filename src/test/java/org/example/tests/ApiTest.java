package org.example.tests;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class ApiTest {

    @Test
    public void apiTest() {

        //Login API
        //Store Payload
        HashMap<Object,Object> loginPayload = new HashMap<>();
        loginPayload.put("email","unknown1@gmail.com");
        loginPayload.put("password","Unknown@1");

        Playwright playwright=Playwright.create();
        APIRequestContext request= playwright.request().newContext();
        APIResponse loginResponse=request.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login", RequestOptions.create().setData(loginPayload));

        Assert.assertTrue(loginResponse.ok(),"Login is successful");
//        System.out.println(loginResponse.text());

        //Read the response and collect the value in token key
        String token=JsonPath.read(loginResponse.text(), "$.token");
        System.out.println("Token: "+token);

        //Create Event
        String title="Postman Event";
        String description="Event for a postman";
        String category= "Workshop";
        String venue= "White perl Hall, Kormagala";
        String city= "Bengaluru";
        String eventDate= "2026-09-15T06:29:00.000Z";
        int price= 200;
        int totalSeats= 100;

        HashMap<Object,Object> createEventPayload = new HashMap<>();
        createEventPayload.put("title",title);
        createEventPayload.put("description",description);
        createEventPayload.put("category",category);
        createEventPayload.put("venue",venue);
        createEventPayload.put("city",city);
        createEventPayload.put("eventDate",eventDate);
        createEventPayload.put("price",price);
        createEventPayload.put("totalSeats",totalSeats);

        APIResponse createEventResponse=request.post("https://api.eventhub.rahulshettyacademy.com/api/events", RequestOptions.create().setHeader("Authorization", "Bearer "+token)
                .setData(createEventPayload));
        Assert.assertTrue(createEventResponse.ok(),"Event creation failed");
//        System.out.println(createEventResponse.text());
        int eventId=JsonPath.read(createEventResponse.text(),"$.data.id");
        System.out.println("Event ID: "+eventId);

        //Get events
        APIResponse getEventResponse=request.get("https://api.eventhub.rahulshettyacademy.com/api/events", RequestOptions.create().setQueryParam("page",1)
                .setQueryParam("limit",12).setHeader("Authorization", "Bearer "+token));
        Assert.assertTrue(getEventResponse.ok(),"Events is not retrived successfully");
        List<Integer> eventsIdList=JsonPath.read(getEventResponse.text(),"$.data[*].id");
        Assert.assertTrue(eventsIdList.contains(eventId),"Event ID is not found");

        //Delete event
        APIResponse deleteEventResponse=request.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,RequestOptions.create()
                .setHeader("Authorization", "Bearer "+token));
        Assert.assertTrue(deleteEventResponse.ok(),"Event deletion failed");
//        System.out.println(deleteEventResponse.text());

        //Verify the event id doesn't exist by calling getEvent request
        APIResponse verifyResponse= request.get("https://api.eventhub.rahulshettyacademy.com/api/events", RequestOptions.create().setQueryParam("page",1)
                .setQueryParam("limit",12).setHeader("Authorization", "Bearer "+token));
        Assert.assertTrue(verifyResponse.ok(),"Events is not retrived successfully");
        List<Integer> allEventIds=JsonPath.read(verifyResponse.text(),"$.data[*].id");
        Assert.assertFalse(allEventIds.contains(eventId),"Event id is still present");



    }
}
