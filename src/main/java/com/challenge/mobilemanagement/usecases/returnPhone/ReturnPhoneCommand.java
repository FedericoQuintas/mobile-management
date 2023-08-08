package com.challenge.mobilemanagement.usecases.returnPhone;

import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.domain.Username;

public record ReturnPhoneCommand(PhoneModel phoneModel, Username username)  {

    public static ReturnPhoneCommand fromRequest(ReturnPhoneRequest returnPhoneRequest) {
        return new ReturnPhoneCommand(PhoneModel.of(returnPhoneRequest.phone()), Username.of(returnPhoneRequest.username()));
    }
}
