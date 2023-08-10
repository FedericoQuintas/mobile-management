package com.challenge.mobilemanagement.domain;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public class PhoneEvents {
    private final List<PhoneEvent> phoneEvents;
    private final PhoneModel phoneModel;

    public PhoneEvents(List<PhoneEvent> phoneEvents, PhoneModel phoneModel) {
        this.phoneEvents = phoneEvents.stream().sorted(Comparator.comparing(PhoneEvent::version)).toList();
        this.phoneModel = phoneModel;
    }

    public static PhoneEvents of(List<PhoneEvent> phoneEvents, PhoneModel phoneModel) {
        return new PhoneEvents(phoneEvents, phoneModel);
    }

    public boolean isBooked() {
        if (phoneEvents.isEmpty()) return false;
        PhoneEvent latestPhoneEvent = fetchLastEvent();
        return latestPhoneEvent.isBookingEvent();
    }

    public Version getNextVersion() {
        return Version.of(phoneEvents.size() + 1);
    }

    public PhoneStatus currentStatus() {
        if (isBooked())
            return PhoneStatus.of(phoneModel, Availability.BOOKED, holder(), lastTimeBooked());
        return PhoneStatus.of(phoneModel, Availability.AVAILABLE);
    }

    private Username holder() {
        if (phoneEvents.isEmpty()) return Username.EMPTY;
        return fetchLastEvent().username();
    }

    private PhoneEvent fetchLastEvent() {
        return phoneEvents.get(phoneEvents.size() - 1);
    }

    private Instant lastTimeBooked() {
        return fetchLastEvent().timestamp();
    }
}
