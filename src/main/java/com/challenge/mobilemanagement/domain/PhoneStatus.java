package com.challenge.mobilemanagement.domain;

import java.time.Instant;

public record PhoneStatus(PhoneModel model, Availability availability, Username holder, Instant bookedTime) {
}
