package com.challenge.mobilemanagement.api.responses;

import com.challenge.mobilemanagement.domain.events.PhoneEvent;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertEquals;

public class PhoneEventResponseTest {

    @Test
    public void convertsResponseFromEvent() {
        PhoneEvent event = TestPhoneEventBuilder.builder().build();
        PhoneEventResponse phoneEventResponse = PhoneEventResponse.from(event);
        assertEquals(event.phoneModel().model(), phoneEventResponse.model());
        assertEquals(event.eventType().name(), phoneEventResponse.eventType());
        assertEquals(event.username().value(), phoneEventResponse.username());
        assertEquals(event.timestamp(), phoneEventResponse.timestamp());
    }
}
