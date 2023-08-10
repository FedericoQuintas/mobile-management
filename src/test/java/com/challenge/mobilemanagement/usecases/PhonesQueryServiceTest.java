package com.challenge.mobilemanagement.usecases;

import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
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

        PhoneEvent returnedEvent = TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).with(Version.of(2)).build();
        when(phoneEventsStream.findAllById(List.of(phoneModel().model())))
                .thenReturn(Flux.fromStream(Stream.of(returnedEvent.asPersistentModel(), buildBookedEvent().asPersistentModel())));

        phonesQueryService = new PhonesQueryService(phoneEventsStream, phoneRepository);

        StepVerifier.create(phonesQueryService.fetchHistory(phoneModel()))
                .expectNextMatches(element -> element.equals(buildBookedEvent()))
                .expectNextMatches(element -> element.equals(returnedEvent))
                .verifyComplete();

    }

}
