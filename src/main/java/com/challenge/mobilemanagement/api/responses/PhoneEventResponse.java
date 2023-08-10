package com.challenge.mobilemanagement.api.responses;

import com.challenge.mobilemanagement.domain.events.PhoneEvent;

import java.time.Instant;

public record PhoneEventResponse(String model, String eventType, String username, Instant timestamp) {

    public static PhoneEventResponse from(PhoneEvent event) {
        return new PhoneEventResponse(event.phoneModel().model(), event.eventType().name(), event.username().value(),
                event.timestamp());
    }
}
