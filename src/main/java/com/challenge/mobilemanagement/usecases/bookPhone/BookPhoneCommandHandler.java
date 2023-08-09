package com.challenge.mobilemanagement.usecases.bookPhone;

import com.challenge.mobilemanagement.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Clock;
import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

@Service
public class BookPhoneCommandHandler {

    Logger logger = LoggerFactory.getLogger(BookPhoneCommandHandler.class);
    public static final String PHONE_IS_ALREADY_BOOKED = "Phone is already booked";
    public static final String PHONE_HAS_JUST_BEEN_BOOKED = "Phone has just been booked";
    public static final String MODEL_DOES_NOT_EXIST = "Model does not exist";
    private final PhoneEventsStream phoneEventsStream;
    private final Clock clock;
    private final PhoneRepository phoneRepository;

    public BookPhoneCommandHandler(PhoneEventsStream phoneEventsStream, Clock clock, PhoneRepository phoneRepository) {
        this.phoneEventsStream = phoneEventsStream;
        this.clock = clock;
        this.phoneRepository = phoneRepository;
    }

    public Mono<Result> handle(BookPhoneCommand bookPhoneCommand) {
        if (!doesPhoneModelExist(bookPhoneCommand.phoneModel()))
            return Mono.just(Result.unavailable(MODEL_DOES_NOT_EXIST));

        return fetchEventsById(bookPhoneCommand.phoneModel())
                .flatMap(events -> {
                    if (events.isBooked()) return Mono.just(Result.unavailable(PHONE_IS_ALREADY_BOOKED));
                    PhoneEvent phoneEvent = buildPhoneEvent(bookPhoneCommand, events);
                    return addEvent(phoneEvent);
                })
                .doOnError(logError())
                .onErrorReturn(Result.unavailable(PHONE_HAS_JUST_BEEN_BOOKED));
    }

    private boolean doesPhoneModelExist(PhoneModel model) {
        return phoneRepository.exists(model);
    }

    private Consumer<Throwable> logError() {
        return error -> logger.error(error.getMessage());
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
