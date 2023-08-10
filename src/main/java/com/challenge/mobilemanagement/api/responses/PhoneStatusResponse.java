package com.challenge.mobilemanagement.api.responses;

import com.challenge.mobilemanagement.domain.status.PhoneStatus;
import com.challenge.mobilemanagement.domain.Username;

import java.time.Instant;

public record PhoneStatusResponse(String model, String username, String availability, Instant timestamp) {

    public static PhoneStatusResponse from(PhoneStatus event) {
        return new PhoneStatusResponse(event.model().model(), event.holder().orElse(Username.EMPTY).value(), event.availability().name(),
                event.bookedTime().orElse(null));
    }
}
