package com.challenge.mobilemanagement.helper;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.domain.*;
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

    public static Username username(){
        return Username.of(USERNAME);
    }

    public static BookPhoneCommand buildBookPhoneCommand(){
        return BookPhoneCommand.fromRequest(new BookPhoneRequest(PHONE_ID, USERNAME));
    }

    public static BookPhoneCommand buildBookPhoneCommand(String user){
        return BookPhoneCommand.fromRequest(new BookPhoneRequest(PHONE_ID, user));
    }

    public static Clock clock(){
        return Clock.fixed(Instant.parse("2023-01-01T00:00:00Z"), ZoneOffset.UTC);
    }

    public static ReturnPhoneCommand buildReturnPhoneCommand() {
        return ReturnPhoneCommand.fromRequest(new ReturnPhoneRequest(PHONE_ID, USERNAME));
    }
}
