package com.example;

import com.example.constraints.ConsistentDataParameters;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Value;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.EventReminder;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

@RestController
@Validated
public class EventPlanner {

    private static final Log logger = LogFactory.getLog(EventPlanner.class);
    private static final String APPLICATION_NAME = "";
    private static HttpTransport httpTransport;
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static Calendar service;

    GoogleClientSecrets clientSecrets;
    GoogleAuthorizationCodeFlow codeFlow;
    Credential credential;

    @Value("${google.client.client-id}")
    private String clientId;
    @Value("${google.client.client-secret}")
    private String clientSecret;
    @Value("${google.client.redirectUri}")
    private String redirectURI;


    @GetMapping("/add-event")
    @ConsistentDataParameters
    public void createAuthorConference(@RequestParam
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                   LocalDateTime  start,
                                       @RequestParam
                                       @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                               LocalDateTime  end,
                                       @RequestParam(value = "code") String code) throws GeneralSecurityException, IOException {

        //httpTransport =  new ApacheHttpTransport();

        codeFlow = new GoogleAuthorizationCodeFlow.Builder(httpTransport, JSON_FACTORY, clientSecrets,
                Collections.singleton(CalendarScopes.CALENDAR)).build();

        TokenResponse response = codeFlow.newTokenRequest(code).setRedirectUri(redirectURI).execute();
        credential = codeFlow.createAndStoreCredential(response, "userID");
        service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();


        Event event = new Event()
                .setSummary("Everyday appraisal")
                .setLocation("Warsaw")
                .setDescription("A chance to be better person");

        EventDateTime startDate = new EventDateTime()
                .setDateTime(DateTime.parseRfc3339(start.toString()))
                .setTimeZone("Europe/Warsaw");

        EventDateTime endDate = new EventDateTime()
                .setDateTime(DateTime.parseRfc3339(end.toString()))
                .setTimeZone("Europe/Warsaw");

        event.setStart(startDate);
        event.setStart(endDate);

        EventAttendee attendee = new EventAttendee().setEmail("salamonikp@gmail.com");
        event.setAttendees(Arrays.asList(attendee));

        EventReminder reminderOverrides = new EventReminder()
                .setMethod("email").setMinutes(30)
                .setMethod("popup").setMinutes(10);
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(false)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);

        String calendarId = "";
        event = service.events().insert(calendarId, event).execute();
        System.out.println("Event created");

        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }
}
