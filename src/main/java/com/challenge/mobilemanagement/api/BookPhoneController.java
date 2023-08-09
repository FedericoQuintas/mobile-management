package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.requests.BookPhoneRequest;
import com.challenge.mobilemanagement.api.responses.ResultResponse;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommand;
import com.challenge.mobilemanagement.usecases.bookPhone.BookPhoneCommandHandler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class BookPhoneController {

    private final BookPhoneCommandHandler bookPhoneCommandHandler;

    public BookPhoneController(BookPhoneCommandHandler bookPhoneCommandHandler) {
        this.bookPhoneCommandHandler = bookPhoneCommandHandler;
    }

    @PostMapping("/book")
    public Mono<ResponseEntity<ResultResponse>> create(@RequestBody BookPhoneRequest bookPhoneRequest) {

        return bookPhoneCommandHandler.handle(BookPhoneCommand.fromRequest(bookPhoneRequest))
                .flatMap(result -> {
                    if (result.isSuccessful()) return Mono.just(ResponseEntity.ok().build());
                    return Mono.just(ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                            .body(new ResultResponse(result.message())));
                });
    }
}
