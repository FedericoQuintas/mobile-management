package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.domain.Result;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommandHandler;
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
public class BookPhoneControllerTest {

    @MockBean
    BookPhoneCommandHandler bookPhoneCommandHandler;

    @Autowired
    WebTestClient webClient;

    @Test
    public void returns200WhenBooksSuccessfully() {

        when(bookPhoneCommandHandler.handle(any())).thenReturn(Mono.just(Result.ok()));

        webClient
                .post().uri("/book")
                .body(Mono.just(buildBookPhoneRequest(username().value())), BookPhoneRequest.class)
                .exchange()
                .expectStatus().isOk();

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();
        verify(bookPhoneCommandHandler).handle(bookPhoneCommand);
    }

    @Test
    public void returns400WhenBookingFails() {

        when(bookPhoneCommandHandler.handle(any())).thenReturn(Mono.just(Result.unavailable("Error message")));

        webClient
                .post().uri("/book")
                .body(Mono.just(buildBookPhoneRequest(username().value())), BookPhoneRequest.class)
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.message").isEqualTo("Error message");

        BookPhoneCommand bookPhoneCommand = buildBookPhoneCommand();
        verify(bookPhoneCommandHandler).handle(bookPhoneCommand);
    }

}
