package com.challenge.mobilemanagement.infrastructure;

import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.domain.PhoneRepository;
import com.challenge.mobilemanagement.domain.phone.Phone;
import com.challenge.mobilemanagement.domain.phone.Technologies;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Set;

@Repository
public class InMemoryPhoneRepository implements PhoneRepository {

    Set<Phone> phones = Set.of(
            Phone.of(PhoneModel.of("Samsung Galaxy S9"), Technologies.of(List.of("GSM"))),
            Phone.of(PhoneModel.of("2x Samsung Galaxy S8"), Technologies.of(List.of("4G, 5G"))),
            Phone.of(PhoneModel.of("Motorola Nexus 6"), Technologies.of(List.of("GSM, 3G"))),
            Phone.of(PhoneModel.of("Oneplus 9"), Technologies.of(List.of("GSM, 4G, 5G"))),
            Phone.of(PhoneModel.of("Apple iPhone 13"), Technologies.of(List.of("3G, 5G"))),
            Phone.of(PhoneModel.of("Apple iPhone 12"), Technologies.of(List.of("5G"))),
            Phone.of(PhoneModel.of("Apple iPhone 11"), Technologies.of(List.of("4G, 5G"))),
            Phone.of(PhoneModel.of("iPhone X"), Technologies.of(List.of("3G, 4G"))),
            Phone.of(PhoneModel.of("Nokia 3310"), Technologies.of(List.of("4G, 5G")))
    );

    @Override
    public Boolean exists(PhoneModel model) {
        return phones.stream().anyMatch(phone -> phone.model().equals(model));
    }

    @Override
    public Flux<PhoneModel> fetchModels() {
        return Flux.fromIterable(phones).map(Phone::model);
    }

    @Override
    public Flux<Phone> fetchPhonesDetails() {
        return Flux.fromIterable(phones);
    }
}
