package com.challenge.mobilemanagement.domain;

public record Result(Boolean isSuccessful, String message) {
    public static Result unavailable() {
        return new Result(false, "Phone is unavailable");
    }

    public static Result ok() {
        return new Result(true, "");
    }

}
