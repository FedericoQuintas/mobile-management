package com.challenge.mobilemanagement.usecases.bookPhone;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.domain.Username;

public record BookPhoneCommand(PhoneModel phoneModel, Username username) {

    public static BookPhoneCommand fromRequest(BookPhoneRequest bookPhoneRequest) {
        return new BookPhoneCommand(PhoneModel.of(bookPhoneRequest.phone()), Username.of(bookPhoneRequest.user()));
    }
}
