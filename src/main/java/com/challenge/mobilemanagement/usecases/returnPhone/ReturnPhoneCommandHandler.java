package com.challenge.mobilemanagement.usecases.returnPhone;

import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.domain.events.PhoneEvent;
import com.challenge.mobilemanagement.domain.events.PhoneEventType;
import com.challenge.mobilemanagement.domain.events.PhoneEvents;
import com.challenge.mobilemanagement.domain.events.PhoneEventsStream;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

@Service
public class ReturnPhoneCommandHandler {

    Logger logger = LoggerFactory.getLogger(BookPhoneCommandHandler.class);
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
                })
                .doOnError(logError())
                .onErrorReturn(Result.unavailable(PHONE_HAS_JUST_BEEN_RETURNED));
    }

    private Consumer<Throwable> logError() {
        return error -> logger.error(error.getMessage());
    }

    private PhoneEvent buildPhoneEvent(ReturnPhoneCommand returnPhoneCommand, PhoneEvents events) {
        return PhoneEvent.of(returnPhoneCommand.phoneModel(), returnPhoneCommand.username(),
                PhoneEventType.RETURNED, events.getNextVersion(), Instant.now(clock));
    }

    private Mono<Result> addEvent(PhoneEvent phoneEvent) {
        return phoneEventsStream.save(phoneEvent.asPersistentModel()).then(Mono.just(Result.ok()));
    }

    private Mono<PhoneEvents> fetchEventsById(PhoneModel phoneModel) {
        return phoneEventsStream.findAllById(List.of(phoneModel.model())).map(PhoneEvent::from).collectList()
                .map(phoneEvents -> PhoneEvents.of(phoneEvents, phoneModel));
    }
}
