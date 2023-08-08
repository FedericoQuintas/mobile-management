package com.challenge.mobilemanagement.domain;

import reactor.core.publisher.Mono;

public interface PhoneEventsStream {
    Mono<Void> add(PhoneEvent phoneEvent);

    Mono<PhoneEvents> findById(PhoneId phoneId);
}
