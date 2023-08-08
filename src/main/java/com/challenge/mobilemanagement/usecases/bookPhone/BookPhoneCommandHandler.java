package com.challenge.mobilemanagement.usecases.bookPhone;

import com.challenge.mobilemanagement.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class BookPhoneCommandHandler {
    public static final String PHONE_IS_ALREADY_BOOKED = "Phone is already booked";
    public static final String PHONE_HAS_JUST_BEEN_BOOKED = "Phone has just been booked";
    private final PhoneEventsStream phoneEventsStream;

    public BookPhoneCommandHandler(PhoneEventsStream phoneEventsStream) {
        this.phoneEventsStream = phoneEventsStream;
    }

    public Mono<Result> handle(BookPhoneCommand bookPhoneCommand) {

        return fetchEventsById(bookPhoneCommand.phoneId())
                .flatMap(events -> {
                    if (events.isBooked()) return Mono.just(Result.unavailable(PHONE_IS_ALREADY_BOOKED));
                    PhoneEvent phoneEvent = PhoneEvent.of(bookPhoneCommand.phoneId(), bookPhoneCommand.username(), PhoneEventType.BOOKED, events.getNextVersion());
                    return addEvent(phoneEvent);
                }).onErrorReturn(Result.unavailable(PHONE_HAS_JUST_BEEN_BOOKED));
    }

    private Mono<Result> addEvent(PhoneEvent phoneEvent) {
        return phoneEventsStream.add(phoneEvent).then(Mono.just(Result.ok()));
    }

    private Mono<PhoneEvents> fetchEventsById(PhoneId phoneId) {
        return phoneEventsStream.findById(phoneId);
    }
}
