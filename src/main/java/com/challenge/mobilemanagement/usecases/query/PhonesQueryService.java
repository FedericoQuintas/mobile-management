package com.challenge.mobilemanagement.usecases.query;

import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.domain.events.PhoneEvent;
import com.challenge.mobilemanagement.domain.events.PhoneEvents;
import com.challenge.mobilemanagement.domain.events.PhoneEventsStream;
import com.challenge.mobilemanagement.domain.phone.Phone;
import com.challenge.mobilemanagement.domain.status.PhoneStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;

@Service
public class PhonesQueryService {
    private final PhoneEventsStream phoneEventsStream;
    private final PhoneRepository phoneRepository;

    public PhonesQueryService(PhoneEventsStream phoneEventsStream, PhoneRepository phoneRepository) {
        this.phoneEventsStream = phoneEventsStream;
        this.phoneRepository = phoneRepository;
    }

    public Flux<PhoneStatus> fetchAll() {
        return phoneRepository.fetchModels()
                .flatMap(model -> phoneEventsStream.findAllById(List.of(model.model())).map(PhoneEvent::from)
                        .collectList()
                        .map(entry -> PhoneEvents.of(entry, model).currentStatus()));

    }

    public Flux<PhoneEvent> fetchHistory(PhoneModel phoneModel) {
        return phoneEventsStream.findAllById(List.of(phoneModel.model())).map(PhoneEvent::from).sort();
    }

    public Flux<Phone> fetchPhonesDetails() {
        return phoneRepository.fetchPhonesDetails();
    }
}
