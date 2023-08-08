package com.challenge.mobilemanagement.domain;

public record Username(String value) {

    public static Username of(String username) {
        return new Username(username);
    }
}
