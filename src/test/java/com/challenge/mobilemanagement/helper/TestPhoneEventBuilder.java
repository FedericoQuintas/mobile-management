package com.challenge.mobilemanagement.helper;

import com.challenge.mobilemanagement.domain.*;

import static com.challenge.mobilemanagement.helper.TestHelper.phoneId;
import static com.challenge.mobilemanagement.helper.TestHelper.username;

public class TestPhoneEventBuilder {

    private PhoneId phoneId = phoneId();

    private Username username = username();

    private PhoneEventType eventType = PhoneEventType.BOOKED;

    private Version version = Version.of(1);

    public static TestPhoneEventBuilder builder() {
        return new TestPhoneEventBuilder();
    }

    public PhoneEvent build() {
        return PhoneEvent.of(phoneId, username, eventType, version);
    }

    public TestPhoneEventBuilder with(Version version) {
        this.version = version;
        return this;
    }

    public TestPhoneEventBuilder with(PhoneEventType eventType) {
        this.eventType = eventType;
        return this;
    }
}
