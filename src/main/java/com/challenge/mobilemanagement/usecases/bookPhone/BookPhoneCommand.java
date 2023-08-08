package com.challenge.mobilemanagement.usecases.bookPhone;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.domain.PhoneId;
import com.challenge.mobilemanagement.domain.Username;

public record BookPhoneCommand(PhoneId phoneId, Username username) {

    public static BookPhoneCommand fromRequest(BookPhoneRequest bookPhoneRequest) {
        return new BookPhoneCommand(PhoneId.of(bookPhoneRequest.phone()), Username.of(bookPhoneRequest.user()));
    }
}
