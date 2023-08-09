package com.challenge.mobilemanagement.infra.memory;

import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.infrastructure.InMemoryPhoneRepository;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class InMemoryPhoneRepositoryTest {

    @Test
    public void respondsWhetherPhoneModelExists() {
        InMemoryPhoneRepository inMemoryPhoneRepository = new InMemoryPhoneRepository();
        assertFalse(inMemoryPhoneRepository.exists(new PhoneModel("a")));
        assertTrue(inMemoryPhoneRepository.exists(new PhoneModel("Apple iPhone 13")));
    }
}
