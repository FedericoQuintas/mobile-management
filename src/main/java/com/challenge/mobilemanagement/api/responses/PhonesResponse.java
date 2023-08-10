package com.challenge.mobilemanagement.api.responses;

import com.challenge.mobilemanagement.domain.phone.Phone;

import java.util.List;

public record PhonesResponse(String model, List<String> technologies) {
    public static PhonesResponse from(Phone phone) {
        return new PhonesResponse(phone.model().model(), phone.technologies().values());
    }
}
