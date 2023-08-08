package com.challenge.mobilemanagement.usecases;


import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.Mockito.*;

public class BookPhoneCommandHandlerTest {

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

        StepVerifier.create(bookPhoneCommandHandler.book(bookPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).add(TestPhoneEventBuilder.builder().build());

    }

    @Test
    public void whenBooksForSecondTimeThenAddsBookingEventWithVersionTwo() {

        PhoneEvent eventWithVersionTwo = TestPhoneEventBuilder.builder().with(Version.of(2)).build();
        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of(buildReturnedEvent()))));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();

        StepVerifier.create(bookPhoneCommandHandler.book(bookPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).add(eventWithVersionTwo);

    }

    @Test
    public void whenBooksPhoneButIsAlreadyBookedThenItDoesNotAddBookingEvent() {

        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of(TestPhoneEventBuilder.builder().build()))));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand("User 2");

        StepVerifier.create(bookPhoneCommandHandler.book(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && "Phone is unavailable".equals(result.message()))
                .verifyComplete();

        verify(phoneEventsStream, never()).add(any());

    }

    @Test
    public void whenSomeoneElseBookedConcurrentlyThenVersionConstraintIsTriggered() {

        when(phoneEventsStream.add(any())).thenReturn(Mono.error(new DuplicateVersionException()));

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand("User 2");

        StepVerifier.create(bookPhoneCommandHandler.book(bookPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && "Phone is unavailable".equals(result.message()))
                .verifyComplete();

    }

    private static PhoneEvent buildReturnedEvent() {
        return TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).build();
    }
}
