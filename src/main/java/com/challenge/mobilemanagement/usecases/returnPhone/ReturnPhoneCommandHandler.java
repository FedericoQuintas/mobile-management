package com.challenge.mobilemanagement.usecases.returnPhone;

import com.challenge.mobilemanagement.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class ReturnPhoneCommandHandler {
    public static final String PHONE_WAS_NOT_BOOKED = "Phone was not booked";
    private Clock clock;
    private final PhoneEventsStream phoneEventsStream;
    public static final String PHONE_HAS_JUST_BEEN_RETURNED = "Phone has just been returned";

    public ReturnPhoneCommandHandler(PhoneEventsStream phoneEventsStream, Clock clock) {
        this.phoneEventsStream = phoneEventsStream;
        this.clock = clock;
    }

    public Mono<Result> handle(ReturnPhoneCommand returnPhoneCommand) {
        return fetchEventsById(returnPhoneCommand.phoneModel())
                .flatMap(events -> {
                    if (!events.isBooked()) return Mono.just(Result.unavailable(PHONE_WAS_NOT_BOOKED));
                    PhoneEvent phoneEvent = buildPhoneEvent(returnPhoneCommand, events);
                    return addEvent(phoneEvent);
                }).onErrorReturn(Result.unavailable(PHONE_HAS_JUST_BEEN_RETURNED));
    }

    private PhoneEvent buildPhoneEvent(ReturnPhoneCommand returnPhoneCommand, PhoneEvents events) {
        return PhoneEvent.of(returnPhoneCommand.phoneModel(), returnPhoneCommand.username(),
                PhoneEventType.RETURNED, events.getNextVersion(), Instant.now(clock));
    }

    private Mono<Result> addEvent(PhoneEvent phoneEvent) {
        return phoneEventsStream.save(phoneEvent.asPersistentModel()).then(Mono.just(Result.ok()));
    }

    private Mono<PhoneEvents> fetchEventsById(PhoneModel phoneModel) {
        return phoneEventsStream.findAllById(List.of(phoneModel.model())).map(PhoneEvent::from).collectList().map(PhoneEvents::of);
    }
}
