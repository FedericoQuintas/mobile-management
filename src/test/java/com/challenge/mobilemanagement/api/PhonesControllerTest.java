package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.responses.PhoneEventResponse;
import com.challenge.mobilemanagement.api.responses.PhoneStatusResponse;
import com.challenge.mobilemanagement.api.responses.PhonesResponse;
import com.challenge.mobilemanagement.domain.*;
import com.challenge.mobilemanagement.domain.events.PhoneEvent;
import com.challenge.mobilemanagement.domain.phone.Phone;
import com.challenge.mobilemanagement.domain.phone.Technologies;
import com.challenge.mobilemanagement.domain.status.Availability;
import com.challenge.mobilemanagement.domain.status.PhoneStatus;
import com.challenge.mobilemanagement.usecases.query.PhonesQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Stream;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PhonesControllerTest {

    @MockBean
    PhonesQueryService phonesQueryService;

    @Autowired
    WebTestClient webClient;

    @Test
    public void returnsPhoneEventsHistory() {

        PhoneEvent bookedEvent = buildBookedEvent();
        PhoneEvent returnedEvent = buildReturnedEvent();
        when(phonesQueryService.fetchHistory(any())).thenReturn(Flux.fromIterable(List.of(bookedEvent, returnedEvent)));


        webClient.get().uri("/phones/" + phoneModel().model() + "/history").exchange()
                .expectStatus().isOk()
                .expectBodyList(PhoneEventResponse.class)
                .hasSize(2)
                .contains(PhoneEventResponse.from(bookedEvent), PhoneEventResponse.from(returnedEvent));

    }

    @Test
    public void returnsCurrentStatusForEachPhone() {
        PhoneStatus phoneStatusBooked = PhoneStatus.of(phoneModel(), Availability.BOOKED, username(), clock().instant());
        PhoneStatus phoneStatusAvailable = PhoneStatus.of(PhoneModel.of("Model2"), Availability.AVAILABLE);
        when(phonesQueryService.fetchAll())
                .thenReturn(Flux.fromIterable(List.of(
                        phoneStatusBooked,
                        phoneStatusAvailable)
                ));


        webClient.get().uri("/phones-status").exchange().expectStatus().isOk()
                .expectBodyList(PhoneStatusResponse.class)
                .hasSize(2)
                .contains(PhoneStatusResponse.from(phoneStatusBooked), PhoneStatusResponse.from(phoneStatusAvailable));

    }

    @Test
    public void returnsPhonesDetails() {

        Phone phone = Phone.of(phoneModel(), Technologies.of(List.of("3G")));
        when(phonesQueryService.fetchPhonesDetails()).thenReturn(Flux.fromIterable(List.of(phone)));


        webClient.get().uri("/phones").exchange().expectStatus().isOk()
                .expectBodyList(PhonesResponse.class)
                .hasSize(1)
                .contains(PhonesResponse.from(phone));

    }


}
