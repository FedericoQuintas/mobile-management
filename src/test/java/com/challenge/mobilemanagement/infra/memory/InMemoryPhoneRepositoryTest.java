package com.challenge.mobilemanagement.infra.memory;

import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.infrastructure.InMemoryPhoneRepository;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InMemoryPhoneRepositoryTest {

    @Test
    public void respondsWhetherPhoneModelExists() {
        InMemoryPhoneRepository inMemoryPhoneRepository = new InMemoryPhoneRepository();
        assertFalse(inMemoryPhoneRepository.exists(new PhoneModel("a")));
        assertTrue(inMemoryPhoneRepository.exists(new PhoneModel("Apple iPhone 13")));
    }

    @Test
    public void fetchAllRetrievesEveryPhoneModel() {
        InMemoryPhoneRepository inMemoryPhoneRepository = new InMemoryPhoneRepository();
        StepVerifier.create(inMemoryPhoneRepository.fetchModels())
                .expectNextCount(9)
                .verifyComplete();
    }
}
