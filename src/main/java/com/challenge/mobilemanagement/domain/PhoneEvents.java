package com.challenge.mobilemanagement.domain;

import java.util.Comparator;
import java.util.List;

public class PhoneEvents {
    private final List<PhoneEvent> phoneEvents;

    public PhoneEvents(List<PhoneEvent> phoneEvents) {
        this.phoneEvents = phoneEvents.stream().sorted(Comparator.comparing(PhoneEvent::version)).toList();
    }

    public static PhoneEvents of(List<PhoneEvent> phoneEvents) {
        return new PhoneEvents(phoneEvents);
    }

    public boolean isBooked() {
        if (phoneEvents.isEmpty()) return false;
        PhoneEvent latestPhoneEvent = phoneEvents.get(phoneEvents.size() - 1);
        return latestPhoneEvent.isBookingEvent();
    }

    public Version getNextVersion() {
        return Version.of(phoneEvents.size() + 1);
    }
}
