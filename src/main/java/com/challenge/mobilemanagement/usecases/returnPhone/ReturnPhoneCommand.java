package com.challenge.mobilemanagement.usecases.returnPhone;

import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.domain.PhoneId;
import com.challenge.mobilemanagement.domain.Username;

public record ReturnPhoneCommand(PhoneId phoneId, Username username)  {

    public static ReturnPhoneCommand fromRequest(ReturnPhoneRequest returnPhoneRequest) {
        return new ReturnPhoneCommand(PhoneId.of(returnPhoneRequest.phone()), Username.of(returnPhoneRequest.username()));
    }
}
