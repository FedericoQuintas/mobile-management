package com.challenge.mobilemanagement.domain;

import reactor.core.publisher.Flux;

public interface PhoneRepository {
    Boolean exists(PhoneModel model);

    Flux<PhoneModel> fetchAll();
}
