package com.challenge.mobilemanagement.domain.events;

import com.challenge.mobilemanagement.domain.Version;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class PhoneEventTest {

    @Test
    public void convertsToAndFromPersistentModel() {
        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        PhoneEventPersistentModel persistentModel = phoneEvent.asPersistentModel();

        assertEquals(persistentModel.getModel(), phoneEvent.phoneModel().model());
        assertEquals(persistentModel.getEvent_type(), phoneEvent.eventType().name());
        assertEquals(persistentModel.getEvent_time(), phoneEvent.timestamp());
        assertEquals(persistentModel.getUsername(), phoneEvent.username().value());
        assertEquals(PhoneEvent.from(persistentModel), phoneEvent);
    }

    @Test
    public void respondsWhetherItIsBookingEvent() {
        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        assertTrue(phoneEvent.isBookingEvent());
        assertFalse(TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).build().isBookingEvent());
    }

    @Test
    public void comparesEventsByVersion() {
        PhoneEvent firstEvent = TestPhoneEventBuilder.builder().build();
        PhoneEvent secondEvent = TestPhoneEventBuilder.builder().with(Version.of(2)).build();
        assertEquals(-1, firstEvent.compareTo(secondEvent));

    }
}
