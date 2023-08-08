package com.challenge.mobilemanagement.usecases;

import com.challenge.mobilemanagement.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BookPhoneCommandHandler {
    private final PhoneEventsStream phoneEventsStream;

    public BookPhoneCommandHandler(PhoneEventsStream phoneEventsStream) {
        this.phoneEventsStream = phoneEventsStream;
    }

    public Mono<Result> book(BookPhoneCommand bookPhoneCommand) {

        return fetchEventsById(bookPhoneCommand.phoneId())
                .flatMap(events -> {
                    if (events.isBooked()) return Mono.just(Result.unavailable());
                    PhoneEvent phoneEvent = PhoneEvent.of(bookPhoneCommand.phoneId(), bookPhoneCommand.username(), PhoneEventType.BOOKED, events.getNextVersion());
                    return addEvent(phoneEvent);
                }).onErrorReturn(Result.unavailable());
    }

    private Mono<Result> addEvent(PhoneEvent phoneEvent) {
        return phoneEventsStream.add(phoneEvent).then(Mono.just(Result.ok()));
    }

    private Mono<PhoneEvents> fetchEventsById(PhoneId phoneId) {
        return phoneEventsStream.findById(phoneId);
    }
}
