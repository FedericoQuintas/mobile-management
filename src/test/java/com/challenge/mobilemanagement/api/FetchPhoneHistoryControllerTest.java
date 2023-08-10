package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.responses.PhoneEventResponse;
import com.challenge.mobilemanagement.domain.PhoneEvent;
import com.challenge.mobilemanagement.usecases.PhonesQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FetchPhoneHistoryControllerTest {

    @MockBean
    PhonesQueryService phonesQueryService;

    @Autowired
    WebTestClient webClient;

    @Test
    public void returns200WhenReturnsSuccessfully() {

        PhoneEvent bookedEvent = buildBookedEvent();
        PhoneEvent returnedEvent = buildReturnedEvent();
        when(phonesQueryService.fetchHistory(any())).thenReturn(Flux.fromIterable(List.of(bookedEvent,
                returnedEvent)));


        webClient
                .get().uri("/phones/" + phoneModel().model() + "/history")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PhoneEventResponse.class)
                .hasSize(2)
                .contains(PhoneEventResponse.from(bookedEvent), PhoneEventResponse.from(returnedEvent));

    }


}
