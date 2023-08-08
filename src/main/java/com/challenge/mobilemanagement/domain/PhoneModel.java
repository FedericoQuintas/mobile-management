package com.challenge.mobilemanagement.domain;

public record PhoneModel(String model) {
    public static PhoneModel of(String model) {
        return new PhoneModel(model);
    }
}
