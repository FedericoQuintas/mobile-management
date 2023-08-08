package com.challenge.mobilemanagement.usecases.returnPhone;

import com.challenge.mobilemanagement.domain.*;
import reactor.core.publisher.Mono;

public class ReturnPhoneCommandHandler {
    public static final String PHONE_WAS_NOT_BOOKED = "Phone was not booked";
    private final PhoneEventsStream phoneEventsStream;
    public static final String PHONE_HAS_JUST_BEEN_RETURNED = "Phone has just been returned";
    public ReturnPhoneCommandHandler(PhoneEventsStream phoneEventsStream) {
        this.phoneEventsStream = phoneEventsStream;
    }

    public Mono<Result> handle(ReturnPhoneCommand returnPhoneCommand) {

        return fetchEventsById(returnPhoneCommand.phoneId())
                .flatMap(events -> {
                    if (!events.isBooked()) return Mono.just(Result.unavailable(PHONE_WAS_NOT_BOOKED));
                    PhoneEvent phoneEvent = PhoneEvent.of(returnPhoneCommand.phoneId(), returnPhoneCommand.username(), PhoneEventType.RETURNED, events.getNextVersion());
                    return addEvent(phoneEvent);
                }).onErrorReturn(Result.unavailable(PHONE_HAS_JUST_BEEN_RETURNED));
    }

    private Mono<Result> addEvent(PhoneEvent phoneEvent) {
        return phoneEventsStream.add(phoneEvent).then(Mono.just(Result.ok()));
    }

    private Mono<PhoneEvents> fetchEventsById(PhoneId phoneId) {
        return phoneEventsStream.findById(phoneId);
    }
}
