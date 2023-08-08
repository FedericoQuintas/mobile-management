package com.challenge.mobilemanagement.domain;

public record PhoneId(String id) {
    public static PhoneId of(String phone) {
        return new PhoneId(phone);
    }
}
