package com.challenge.mobilemanagement.domain;

import java.time.Instant;
import java.util.Optional;

public record PhoneStatus(PhoneModel model, Availability availability, Optional<Username> holder,
                          Optional<Instant> bookedTime) {
    public static PhoneStatus of(PhoneModel model, Availability availability, Username holder, Instant instant) {
        return new PhoneStatus(model, availability, Optional.of(holder), Optional.of(instant));
    }

    public static PhoneStatus of(PhoneModel model, Availability availability) {
        return new PhoneStatus(model, availability, Optional.empty(), Optional.empty());
    }
}
