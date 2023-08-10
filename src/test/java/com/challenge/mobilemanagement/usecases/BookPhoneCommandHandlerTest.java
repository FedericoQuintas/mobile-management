package com.challenge.mobilemanagement.usecases;


import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.Mockito.*;

public class BookPhoneCommandHandlerTest {

    public static final String PHONE_IS_ALREADY_BOOKED = "Phone is already booked";
    public static final String PHONE_HAS_JUST_BEEN_BOOKED = "Phone has just been booked";
    public static final String MODEL_DOES_NOT_EXIST = "Model does not exist";
    private PhoneEventsStream phoneEventsStream;
    private BookPhoneCommandHandler bookPhoneCommandHandler;
    private PhoneRepository phoneRepository;

    @BeforeEach
    public void before() {
        phoneRepository = mock(PhoneRepository.class);
        phoneEventsStream = mock(PhoneEventsStream.class);
        bookPhoneCommandHandler = new BookPhoneCommandHandler(phoneEventsStream, clock(), phoneRepository);
        when(phoneEventsStream.findAllById(List.of(phoneModel().model()))).thenReturn(Flux.fromIterable((List.of())));
        when(phoneEventsStream.save(any())).thenReturn(Mono.empty());
        when(phoneRepository.exists(any())).thenReturn(true);
    }

    @Test
    public void whenBooksPhoneThenAddsBookingEvent() {

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).save(TestPhoneEventBuilder.builder().build().asPersistentModel());

    }

    @Test
    public void whenBooksForSecondTimeThenAddsBookingEventWithVersionTwo() {

        PhoneEvent eventWithVersionTwo = TestPhoneEventBuilder.builder().with(Version.of(2)).build();
        when(phoneEventsStream.findAllById(List.of(phoneModel().model()))).thenReturn(Flux.fromIterable(List.of(buildReturnedEvent().asPersistentModel())));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).save(eventWithVersionTwo.asPersistentModel());

    }

    @Test
    public void whenBooksPhoneButIsAlreadyBookedThenItDoesNotAddBookingEvent() {

        when(phoneEventsStream.findAllById(List.of(phoneModel().model()))).thenReturn(Flux.fromIterable(List.of(TestPhoneEventBuilder.builder().build().asPersistentModel())));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand("User 2");

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_IS_ALREADY_BOOKED.equals(result.message()))
                .verifyComplete();

        verify(phoneEventsStream, never()).save(any());

    }

    @Test
    public void whenSomeoneElseBookedConcurrentlyThenVersionConstraintIsTriggered() {

        when(phoneEventsStream.save(any())).thenReturn(Mono.error(new RuntimeException()));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand("User 2");

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_HAS_JUST_BEEN_BOOKED.equals(result.message()))
                .verifyComplete();

    }

    @Test
    public void whenBookDoesNotExistThenReturnsError() {

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();

        when(phoneRepository.exists(bookPhoneCommand.phoneModel())).thenReturn(false);

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && MODEL_DOES_NOT_EXIST.equals(result.message()))
                .verifyComplete();

        verify(phoneEventsStream, never()).save(any());
    }

}
