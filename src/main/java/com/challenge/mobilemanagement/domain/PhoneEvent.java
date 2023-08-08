package com.challenge.mobilemanagement.domain;

public record PhoneEvent(PhoneModel phoneModel, Username username, PhoneEventType eventType, Version version) {

    public static PhoneEvent of(PhoneModel phoneModel, Username username, PhoneEventType eventType, Version version) {
        return new PhoneEvent(phoneModel, username, eventType, version);
    }

    public boolean isBookingEvent() {
        return PhoneEventType.BOOKED.equals(eventType);
    }
}
