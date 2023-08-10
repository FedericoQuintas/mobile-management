package com.challenge.mobilemanagement.domain.events;

import com.challenge.mobilemanagement.domain.events.PhoneEventPersistentModel;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PhoneEventsStream extends ReactiveCrudRepository<PhoneEventPersistentModel, String> {
}
