package com.challenge.mobilemanagement.helper;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.domain.events.PhoneEvent;
import com.challenge.mobilemanagement.domain.events.PhoneEventType;
import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.domain.Username;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommand;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

public class TestHelper {

    public static final String PHONE_ID = "Phone_x";
    public static final String USERNAME = "User1";

    public static PhoneModel phoneModel() {
        return PhoneModel.of(PHONE_ID);
    }

    public static Username username() {
        return Username.of(USERNAME);
    }

    public static BookPhoneCommand buildBookPhoneCommand() {
        return BookPhoneCommand.fromRequest(buildBookPhoneRequest(USERNAME));
    }

    public static BookPhoneCommand buildBookPhoneCommand(String user) {
        return BookPhoneCommand.fromRequest(buildBookPhoneRequest(user));
    }

    public static BookPhoneRequest buildBookPhoneRequest(String user) {
        return new BookPhoneRequest(PHONE_ID, user);
    }

    public static Clock clock() {
        return Clock.fixed(Instant.parse("2023-01-01T00:00:00Z"), ZoneOffset.UTC);
    }

    public static ReturnPhoneCommand buildReturnPhoneCommand() {
        return ReturnPhoneCommand.fromRequest(new ReturnPhoneRequest(PHONE_ID, USERNAME));
    }

    public static ReturnPhoneRequest buildReturnPhoneRequest() {
        return new ReturnPhoneRequest(PHONE_ID, USERNAME);
    }

    public static PhoneEvent buildReturnedEvent() {
        return TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).build();
    }

    public static PhoneEvent buildBookedEvent() {
        return TestPhoneEventBuilder.builder().build();
    }
}
