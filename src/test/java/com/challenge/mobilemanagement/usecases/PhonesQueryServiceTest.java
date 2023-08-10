package com.challenge.mobilemanagement.usecases;

import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PhonesQueryServiceTest {

    public static final String MODEL_2 = "model2";
    public static final String MODEL_3 = "model3";
    private PhoneEventsStream phoneEventsStream;
    private PhoneRepository phoneRepository;
    private PhonesQueryService phonesQueryService;

    @BeforeEach
    public void before() {
        phoneEventsStream = mock(PhoneEventsStream.class);
        phoneRepository = mock(PhoneRepository.class);
        phonesQueryService = new PhonesQueryService(phoneEventsStream, phoneRepository);
    }

    @Test
    public void fetchCurrentStatusForEveryModel() {
        PhoneModel secondModel = PhoneModel.of(MODEL_2);
        PhoneModel thirdModel = PhoneModel.of(MODEL_3);
        when(phoneRepository.fetchAll()).thenReturn(Flux.fromIterable(List.of(phoneModel(), secondModel, thirdModel)));
        when(phoneEventsStream.findAllById(List.of(phoneModel().model())))
                .thenReturn(Flux.fromIterable(List.of(buildBookedEvent().asPersistentModel())));
        when(phoneEventsStream.findAllById(List.of(MODEL_2)))
                .thenReturn(Flux.fromIterable(List.of(buildBookedEvent().asPersistentModel(), buildReturnedEvent().asPersistentModel())));
        when(phoneEventsStream.findAllById(List.of(MODEL_3)))
                .thenReturn(Flux.fromIterable(List.of()));

        StepVerifier.create(phonesQueryService.fetchAll())
                .expectNextMatches(element -> element.model().equals(phoneModel()) &&
                        element.availability().equals(Availability.BOOKED) &&
                        element.holder().equals(Optional.of(username())) &&
                        element.bookedTime().equals(Optional.of(clock().instant())))
                .expectNextMatches(element -> element.model().equals(secondModel) &&
                        element.availability().equals(Availability.AVAILABLE) &&
                        element.holder().equals(Optional.empty()) &&
                        element.bookedTime().equals(Optional.empty()))
                .expectNextMatches(element -> element.model().equals(thirdModel) &&
                        element.availability().equals(Availability.AVAILABLE) &&
                        element.holder().equals(Optional.empty()) &&
                        element.bookedTime().equals(Optional.empty()))
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
