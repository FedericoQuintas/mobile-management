package com.challenge.mobilemanagement.domain;

import java.time.Instant;

public record PhoneEvent(PhoneModel phoneModel, Username username, PhoneEventType eventType, Version version, Instant timestamp) {

    public static PhoneEvent of(PhoneModel phoneModel, Username username, PhoneEventType eventType, Version version, Instant timestamp) {
        return new PhoneEvent(phoneModel, username, eventType, version, timestamp);
    }

    public static PhoneEvent from(PhoneEventPersistentModel persistentModel) {
        return PhoneEvent.of(PhoneModel.of(persistentModel.getModel()), Username.of(persistentModel.getUsername()),
                PhoneEventType.valueOf(persistentModel.getEvent_type().toUpperCase()), Version.of(persistentModel.getVersion()),
                persistentModel.getEvent_time());
    }

    public boolean isBookingEvent() {
        return PhoneEventType.BOOKED.equals(eventType);
    }

    public PhoneEventPersistentModel asPersistentModel() {
        return new PhoneEventPersistentModel(phoneModel.model(), username.value(), eventType.name(), version.version(), timestamp);
    }
}
