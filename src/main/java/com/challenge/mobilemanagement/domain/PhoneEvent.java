package com.challenge.mobilemanagement.domain;

public record PhoneEvent(PhoneId phoneId, Username username, PhoneEventType eventType, Version version) {

    public static PhoneEvent of(PhoneId phoneId, Username username, PhoneEventType eventType, Version version) {
        return new PhoneEvent(phoneId, username, eventType, version);
    }

    public boolean isBookingEvent() {
        return PhoneEventType.BOOKED.equals(eventType);
    }
}
