package com.challenge.mobilemanagement.domain;

import com.challenge.mobilemanagement.domain.phone.Phone;
import reactor.core.publisher.Flux;

public interface PhoneRepository {
    Boolean exists(PhoneModel model);

    Flux<PhoneModel> fetchModels();

    Flux<Phone> fetchPhonesDetails();
}
