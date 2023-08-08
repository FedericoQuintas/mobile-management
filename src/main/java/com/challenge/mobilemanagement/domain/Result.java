package com.challenge.mobilemanagement.domain;

public record Result(Boolean isSuccessful, String message) {
    public static Result unavailable(String message) {
        return new Result(false, message);
    }

    public static Result ok() {
        return new Result(true, "");
    }

}
