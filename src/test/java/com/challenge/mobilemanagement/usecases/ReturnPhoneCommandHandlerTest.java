package com.challenge.mobilemanagement.usecases;


import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommand;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommandHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
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
        returnPhoneCommandHandler = new ReturnPhoneCommandHandler(phoneEventsStream, clock());
        when(phoneEventsStream.findAllById(List.of(phoneModel().model()))).thenReturn(Flux.fromIterable(List.of(TestPhoneEventBuilder.builder().build().asPersistentModel())));
        when(phoneEventsStream.save(any())).thenReturn(Mono.empty());
    }

    @Test
    public void whenReturnsPhoneThenAddsReturnEvent() {

        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();

        StepVerifier.create(returnPhoneCommandHandler.handle(returnPhoneCommand))
                .expectNextMatches(Result::isSuccessful)
                .verifyComplete();

        verify(phoneEventsStream).save(TestPhoneEventBuilder.builder().with(PhoneEventType.RETURNED).with(Version.of(2)).build().asPersistentModel());
    }

    @Test
    public void returnsErrorWhenReturnsAPhoneThatWasNotBooked() {

        when(phoneEventsStream.findAllById(List.of(phoneModel().model()))).thenReturn(Flux.fromIterable(List.of()));
        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();

        StepVerifier.create(returnPhoneCommandHandler.handle(returnPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_WAS_NOT_BOOKED.equals(result.message()))
                .verifyComplete();

        verify(phoneEventsStream, never()).save(any());
    }

    @Test
    public void whenSomeoneElseReturnedConcurrentlyThenVersionConstraintIsTriggered() {

        when(phoneEventsStream.save(any())).thenReturn(Mono.error(new RuntimeException()));

        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();

        StepVerifier.create(returnPhoneCommandHandler.handle(returnPhoneCommand))
                .expectNextMatches(result -> !result.isSuccessful() && PHONE_HAS_JUST_BEEN_RETURNED.equals(result.message()))
                .verifyComplete();
    }

}
