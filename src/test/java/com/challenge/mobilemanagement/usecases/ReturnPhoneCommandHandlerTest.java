package com.challenge.mobilemanagement.usecases;


import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommand;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.Mockito.*;

public class ReturnPhoneCommandHandlerTest {
    public static final String PHONE_HAS_JUST_BEEN_RETURNED = "Phone has just been returned";
    public static final String PHONE_WAS_NOT_BOOKED = "Phone was not booked";
    private PhoneEventsStream phoneEventsStream;
    private ReturnPhoneCommandHandler returnPhoneCommandHandler;

    @BeforeEach
    public void before() {
        phoneEventsStream = mock(PhoneEventsStream.class);
        returnPhoneCommandHandler = new ReturnPhoneCommandHandler(phoneEventsStream);
        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of(TestPhoneEventBuilder.builder().build()))));
        when(phoneEventsStream.add(any())).thenReturn(Mono.empty());
    }

    @Test
    public void whenReturnsPhoneThenAddsReturnEvent() {

        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();

        StepVerifier.create(returnPhoneCommandHandler.handle(returnPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).add(TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).with(Version.of(2)).build());
    }

    @Test
    public void returnsErrorWhenReturnsAPhoneThatWasNotBooked() {

        when(phoneEventsStream.findById(phoneId())).thenReturn(Mono.just(PhoneEvents.of(List.of())));
        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();

        StepVerifier.create(returnPhoneCommandHandler.handle(returnPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_WAS_NOT_BOOKED.equals(result.message()))
                .verifyComplete();

        verify(phoneEventsStream, never()).add(any());
    }

    @Test
    public void whenSomeoneElseReturnedConcurrentlyThenVersionConstraintIsTriggered() {

        when(phoneEventsStream.add(any())).thenReturn(Mono.error(new DuplicateVersionException()));

        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();

        StepVerifier.create(returnPhoneCommandHandler.handle(returnPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_HAS_JUST_BEEN_RETURNED.equals(result.message()))
                .verifyComplete();
    }

}
