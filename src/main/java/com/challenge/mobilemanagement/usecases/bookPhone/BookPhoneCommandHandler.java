package com.challenge.mobilemanagement.usecases.bookPhone;

import com.challenge.mobilemanagement.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.List;

@Service
public class BookPhoneCommandHandler {
    public static final String PHONE_IS_ALREADY_BOOKED = "Phone is already booked";
    public static final String PHONE_HAS_JUST_BEEN_BOOKED = "Phone has just been booked";
    private final PhoneEventsStream phoneEventsStream;
    private final Clock clock;

    public BookPhoneCommandHandler(PhoneEventsStream phoneEventsStream, Clock clock) {
        this.phoneEventsStream = phoneEventsStream;
        this.clock = clock;

    }

    public Mono<Result> handle(BookPhoneCommand bookPhoneCommand) {

        return fetchEventsById(bookPhoneCommand.phoneModel())
                .flatMap(events -> {
                    if (events.isBooked()) return Mono.just(Result.unavailable(PHONE_IS_ALREADY_BOOKED));
                    PhoneEvent phoneEvent = buildPhoneEvent(bookPhoneCommand, events);
                    return addEvent(phoneEvent);
                }).onErrorReturn(Result.unavailable(PHONE_HAS_JUST_BEEN_BOOKED));
    }

    private PhoneEvent buildPhoneEvent(BookPhoneCommand bookPhoneCommand, PhoneEvents events) {
        return PhoneEvent.of(bookPhoneCommand.phoneModel(), bookPhoneCommand.username(),
                PhoneEventType.BOOKED, events.getNextVersion(),
                Instant.now(clock));
    }

    private Mono<Result> addEvent(PhoneEvent phoneEvent) {
        return phoneEventsStream.save(phoneEvent.asPersistentModel()).then(Mono.just(Result.ok()));
    }

    private Mono<PhoneEvents> fetchEventsById(PhoneModel phoneModel) {
        return phoneEventsStream.findAllById(List.of(phoneModel.model())).map(PhoneEvent::from).collectList().map(PhoneEvents::of);
    }
}
