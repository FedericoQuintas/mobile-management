package com.challenge.mobilemanagement.helper;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.usecases.BookPhoneCommand;

public class TestHelper {

    public static final String PHONE_ID = "Phone_x";
    public static final String USERNAME = "User1";
    public static PhoneId phoneId() {
        return PhoneId.of(PHONE_ID);
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
}
