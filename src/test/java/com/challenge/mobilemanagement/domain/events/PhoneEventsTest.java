package com.challenge.mobilemanagement.domain.events;

import com.challenge.mobilemanagement.domain.Version;
import com.challenge.mobilemanagement.domain.status.Availability;
import com.challenge.mobilemanagement.domain.status.PhoneStatus;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static com.challenge.mobilemanagement.helper.TestHelper.phoneModel;
import static org.junit.Assert.*;

public class PhoneEventsTest {

    @Test
    public void returnsPhoneWithoutEventIsNotBooked() {
        PhoneEvents emptyEvents = PhoneEvents.of(List.of(), phoneModel());
        assertFalse(emptyEvents.isBooked());
    }

    @Test
    public void returnsReturnedPhoneIsNotBooked() {
        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        PhoneEvent returnedPhoneEvent = TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).build();
        PhoneEvents phoneEvents = PhoneEvents.of(List.of(phoneEvent, returnedPhoneEvent), phoneEvent.phoneModel());
        assertFalse(phoneEvents.isBooked());
    }

    @Test
    public void returnsBookedPhoneIsBooked() {
        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        PhoneEvents phoneEvents = PhoneEvents.of(List.of(phoneEvent), phoneEvent.phoneModel());
        assertTrue(phoneEvents.isBooked());
    }

    @Test
    public void getsNextVersion() {
        PhoneEvents emptyEvents = PhoneEvents.of(List.of(), phoneModel());
        assertEquals(Version.of(1), emptyEvents.getNextVersion());

        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        PhoneEvents phoneEvents = PhoneEvents.of(List.of(phoneEvent), phoneEvent.phoneModel());
        assertEquals(Version.of(2), phoneEvents.getNextVersion());

    }

    @Test
    public void capturesStatusForBookedPhone() {
        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        PhoneEvents phoneEvents = PhoneEvents.of(List.of(phoneEvent), phoneEvent.phoneModel());
        PhoneStatus phoneStatus = phoneEvents.currentStatus();

        assertEquals(phoneStatus.bookedTime(), Optional.of(phoneEvent.timestamp()));
        assertEquals(phoneStatus.model(), phoneEvent.phoneModel());
        assertEquals(phoneStatus.holder(), Optional.of(phoneEvent.username()));
        assertEquals(phoneStatus.availability(), Availability.BOOKED);
    }

    @Test
    public void capturesStatusForAvailablePhone() {
        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();
        PhoneEvent returnedPhoneEvent = TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).build();
        PhoneEvents phoneEvents = PhoneEvents.of(List.of(phoneEvent, returnedPhoneEvent), phoneEvent.phoneModel());
        PhoneStatus phoneStatus = phoneEvents.currentStatus();

        assertEquals(phoneStatus.bookedTime(), Optional.empty());
        assertEquals(phoneStatus.model(), phoneEvent.phoneModel());
        assertEquals(phoneStatus.holder(), Optional.empty());
        assertEquals(phoneStatus.availability(), Availability.AVAILABLE);
    }
}
