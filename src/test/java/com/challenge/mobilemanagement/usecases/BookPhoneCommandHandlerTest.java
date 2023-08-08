package com.challenge.mobilemanagement.usecases;


import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.Mockito.*;

public class BookPhoneCommandHandlerTest {

    public static final String PHONE_IS_ALREADY_BOOKED = "Phone is already booked";
    public static final String PHONE_HAS_JUST_BEEN_BOOKED = "Phone has just been booked";
    private PhoneEventsStream phoneEventsStream;
    private BookPhoneCommandHandler bookPhoneCommandHandler;

    @BeforeEach
    public void before() {
        phoneEventsStream = mock(PhoneEventsStream.class);
        bookPhoneCommandHandler = new BookPhoneCommandHandler(phoneEventsStream);
        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of())));
        when(phoneEventsStream.add(any())).thenReturn(Mono.empty());
    }

    @Test
    public void whenBooksPhoneThenAddsBookingEvent() {

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).add(TestPhoneEventBuilder.builder().build());

    }

    @Test
    public void whenBooksForSecondTimeThenAddsBookingEventWithVersionTwo() {

        PhoneEvent eventWithVersionTwo = TestPhoneEventBuilder.builder().with(Version.of(2)).build();
        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of(buildReturnedEvent()))));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).add(eventWithVersionTwo);

    }

    @Test
    public void whenBooksPhoneButIsAlreadyBookedThenItDoesNotAddBookingEvent() {

        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of(TestPhoneEventBuilder.builder().build()))));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand("User 2");

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_IS_ALREADY_BOOKED.equals(result.message()))
                .verifyComplete();

        verify(phoneEventsStream, never()).add(any());

    }

    @Test
    public void whenSomeoneElseBookedConcurrentlyThenVersionConstraintIsTriggered() {

        when(phoneEventsStream.add(any())).thenReturn(Mono.error(new DuplicateVersionException()));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand("User 2");

        StepVerifier.create(bookPhoneCommandHandler.handle(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_HAS_JUST_BEEN_BOOKED.equals(result.message()))
                .verifyComplete();

    }

    private static PhoneEvent buildReturnedEvent() {
        return TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).build();
    }
}
