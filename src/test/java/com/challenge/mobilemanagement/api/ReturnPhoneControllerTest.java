package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.domain.Result;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommandHandler;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommand;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommandHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static com.challenge.mobilemanagement.helper.TestHelper.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ReturnPhoneControllerTest {

    @MockBean
    ReturnPhoneCommandHandler returnPhoneCommandHandler;

    @Autowired
    WebTestClient webClient;

    @Test
    public void returns200WhenReturnsSuccessfully() {

        when(returnPhoneCommandHandler.handle(any())).thenReturn(Mono.just(Result.ok()));

        webClient
                .post().uri("/return")
                .body(Mono.just(buildReturnPhoneRequest()), ReturnPhoneRequest.class)
                .exchange()
                .expectStatus().isOk();

        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();
        verify(returnPhoneCommandHandler).handle(returnPhoneCommand);
    }

    @Test
    public void returns400WhenReturnFails() {

        when(returnPhoneCommandHandler.handle(any())).thenReturn(Mono.just(Result.unavailable("Error message")));

        webClient
                .post().uri("/return")
                .body(Mono.just(buildReturnPhoneRequest()), ReturnPhoneRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Error message");

        ReturnPhoneCommand returnPhoneCommand = buildReturnPhoneCommand();
        verify(returnPhoneCommandHandler).handle(returnPhoneCommand);
    }

}
