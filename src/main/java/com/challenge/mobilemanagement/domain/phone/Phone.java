package com.challenge.mobilemanagement.domain.phone;

import com.challenge.mobilemanagement.domain.PhoneModel;

public record Phone(PhoneModel model, Technologies technologies) {
    public static Phone of(PhoneModel model, Technologies technologies) {
        return new Phone(model, technologies);
    }
}
