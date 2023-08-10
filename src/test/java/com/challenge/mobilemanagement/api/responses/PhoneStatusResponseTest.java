package com.challenge.mobilemanagement.api.responses;

import com.challenge.mobilemanagement.domain.status.Availability;
import com.challenge.mobilemanagement.domain.status.PhoneStatus;
import org.junit.jupiter.api.Test;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.junit.Assert.*;

public class PhoneStatusResponseTest {

    @Test
    public void convertsResponseFromBookedStatus() {
        PhoneStatus phoneStatus = PhoneStatus.of(phoneModel(), Availability.BOOKED, username(), clock().instant());
        PhoneStatusResponse statusResponse = PhoneStatusResponse.from(phoneStatus);
        assertEquals(phoneStatus.model().model(), statusResponse.model());
        assertEquals(phoneStatus.availability().name(), statusResponse.availability());
        assertEquals(phoneStatus.holder().get().value(), statusResponse.username());
        assertEquals(phoneStatus.bookedTime().get(), statusResponse.timestamp());
    }

    @Test
    public void convertsResponseFromAvailableStatus() {
        PhoneStatus phoneStatus = PhoneStatus.of(phoneModel(), Availability.AVAILABLE);
        PhoneStatusResponse statusResponse = PhoneStatusResponse.from(phoneStatus);
        assertEquals(phoneStatus.model().model(), statusResponse.model());
        assertEquals(phoneStatus.availability().name(), statusResponse.availability());
        assertEquals("", statusResponse.username());
        assertNull(statusResponse.timestamp());
    }
}
