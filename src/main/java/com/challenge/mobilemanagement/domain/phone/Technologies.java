package com.challenge.mobilemanagement.domain.phone;

import java.util.List;

public record Technologies(List<String> values) {
    public static Technologies of(List<String> values) {
        return new Technologies(values);
    }
}
