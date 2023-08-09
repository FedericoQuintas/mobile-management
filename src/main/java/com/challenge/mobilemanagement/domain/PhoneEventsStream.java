package com.challenge.mobilemanagement.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PhoneEventsStream extends ReactiveCrudRepository<PhoneEventPersistentModel, String> {
}
