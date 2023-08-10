package com.challenge.mobilemanagement.usecases;

import com.challenge.mobilemanagement.domain.Availability;
import com.challenge.mobilemanagement.domain.PhoneEventsStream;
import com.challenge.mobilemanagement.domain.PhoneRepository;
import com.challenge.mobilemanagement.domain.Username;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.stream.Stream;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PhonesQueryServiceTest {

    private PhoneEventsStream phoneEventsStream;
    private PhoneRepository phoneRepository;
    private PhonesQueryService phonesQueryService;

    @BeforeEach
    public void before() {
        phoneEventsStream = mock(PhoneEventsStream.class);
        phoneRepository = mock(PhoneRepository.class);
        phonesQueryService = new PhonesQueryService(phoneEventsStream, phoneRepository);
    }

    //    @Test
    public void fetchCurrentStatusForAll() {

        StepVerifier.create(phonesQueryService.fetchAll())
                .expectNextMatches(element -> element.model().equals(phoneModel()) &&
                        element.availability().equals(Availability.AVAILABLE) &&
                        element.holder().equals(Username.EMPTY) &&
                        element.bookedTime().equals(clock().instant()))
                .verifyComplete();

    }

    @Test
    public void fetchPhoneHistory() {

        when(phoneEventsStream.findAllById(List.of(phoneModel().model())))
                .thenReturn(Flux.fromStream(Stream.of(buildBookedEvent().asPersistentModel(), buildReturnedEvent().asPersistentModel())));

        phonesQueryService = new PhonesQueryService(phoneEventsStream, phoneRepository);

        StepVerifier.create(phonesQueryService.fetchHistory(phoneModel()))
                .expectNextMatches(element -> element.equals(buildBookedEvent()))
                .expectNextMatches(element -> element.equals(buildReturnedEvent()))
                .verifyComplete();

    }

}
