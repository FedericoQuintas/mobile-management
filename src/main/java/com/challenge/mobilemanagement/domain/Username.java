package com.challenge.mobilemanagement.domain;

public record Username(String value) {

    public static final Username EMPTY = Username.of("");

    public static Username of(String username) {
        return new Username(username);
    }
}
