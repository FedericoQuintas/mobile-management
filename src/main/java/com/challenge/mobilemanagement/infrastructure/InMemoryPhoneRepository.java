package com.challenge.mobilemanagement.infrastructure;

import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.domain.PhoneRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Set;

@Repository
public class InMemoryPhoneRepository implements PhoneRepository {

    Set<String> models = Set.of("Samsung Galaxy S9",
            "2x Samsung Galaxy S8",
            "Motorola Nexus 6",
            "Oneplus 9",
            "Apple iPhone 13",
            "Apple iPhone 12",
            "Apple iPhone 11",
            "iPhone X",
            "Nokia 3310");

    @Override
    public Boolean exists(PhoneModel model) {
        return models.contains(model.model());
    }

    @Override
    public Flux<PhoneModel> fetchAll() {
        return Flux.fromIterable(models).map(PhoneModel::of);
    }
}
