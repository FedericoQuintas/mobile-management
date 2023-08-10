package com.challenge.mobilemanagement.usecases;

import com.challenge.mobilemanagement.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PhonesQueryService {
    private final PhoneEventsStream phoneEventsStream;
    private final PhoneRepository phoneRepository;

    public PhonesQueryService(PhoneEventsStream phoneEventsStream, PhoneRepository phoneRepository) {
        this.phoneEventsStream = phoneEventsStream;
        this.phoneRepository = phoneRepository;
    }

    public Flux<PhoneStatus> fetchAll() {
        return phoneRepository.fetchAll()
                .flatMap(model -> phoneEventsStream.findAllById(List.of(model.model())).map(PhoneEvent::from)
                        .collectList()
                        .map(entry -> PhoneEvents.of(entry, model).currentStatus()));

    }

    public Flux<PhoneEvent> fetchHistory(PhoneModel phoneModel) {
        return phoneEventsStream.findAllById(List.of(phoneModel.model())).map(PhoneEvent::from).sort();
    }
}
