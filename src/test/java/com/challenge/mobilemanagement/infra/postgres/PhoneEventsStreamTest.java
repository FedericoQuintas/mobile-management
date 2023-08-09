package com.challenge.mobilemanagement.infra.postgres;

import com.challenge.mobilemanagement.domain.PhoneEvent;
import com.challenge.mobilemanagement.domain.PhoneEventPersistentModel;
import com.challenge.mobilemanagement.domain.PhoneEventsStream;
import com.challenge.mobilemanagement.helper.TestPhoneEventBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.test.context.ActiveProfiles;
import reactor.test.StepVerifier;

import java.lang.annotation.Annotation;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PhoneEventsStreamTest {

    @Autowired
    PhoneEventsStream phoneEventsStream;

    @BeforeEach
    void setUp() {
        phoneEventsStream.deleteAll().block();
    }

    @Test
    public void addsEvent() {

        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();

        StepVerifier.create(phoneEventsStream.save(phoneEvent.asPersistentModel()))
                .assertNext(Assertions::assertNotNull)
                .expectComplete()
                .verify();

        StepVerifier.create(phoneEventsStream.findAllById(List.of(phoneEvent.phoneModel().model())))
                .expectNextMatches(event -> phoneEvent.asPersistentModel().equals(event))
                .expectComplete()
                .verify();
    }

    @Test
    public void cantAddWithSameVersion() {

        PhoneEvent phoneEvent = TestPhoneEventBuilder.builder().build();

        StepVerifier.create(phoneEventsStream.save(phoneEvent.asPersistentModel()))
                .assertNext(Assertions::assertNotNull)
                .expectComplete()
                .verify();

        StepVerifier.create(phoneEventsStream.save(phoneEvent.asPersistentModel()))
                .verifyError(DuplicateKeyException.class);

    }

    @Test
    public void testCollectionName() {
        Annotation annotation = PhoneEventPersistentModel.class.getAnnotation(Table.class);

        if (annotation instanceof Table) {
            Table table = (Table) annotation;
            assertEquals("phone_events", table.value());
        } else {
            fail("Did not find annotation");
        }
    }
}
