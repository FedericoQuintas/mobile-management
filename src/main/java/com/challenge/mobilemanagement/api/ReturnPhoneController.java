package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.api.responses.ResultResponse;
import com.challenge.mobilemanagement.domain.Result;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommand;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class ReturnPhoneController {

    public static final String PLEASE_CONTACT_OUR_TEAM = "Please contact our team";
    Logger logger = LoggerFactory.getLogger(BookPhoneController.class);
    private final ReturnPhoneCommandHandler returnPhoneCommandHandler;

    public ReturnPhoneController(ReturnPhoneCommandHandler returnPhoneCommandHandler) {
        this.returnPhoneCommandHandler = returnPhoneCommandHandler;
    }

    @PostMapping("/return")
    public Mono<ResponseEntity<ResultResponse>> create(@RequestBody ReturnPhoneRequest returnPhoneRequest) {
        return returnPhoneCommandHandler.handle(ReturnPhoneCommand.fromRequest(returnPhoneRequest))
                .flatMap(ReturnPhoneController::buildResponse)
                .doOnError(error -> logger.error(error.getMessage()))
                .onErrorReturn(handleUnknownError());
    }

    private static ResponseEntity<ResultResponse> handleUnknownError() {
        return ResponseEntity.internalServerError().contentType(MediaType.APPLICATION_JSON)
                .body(new ResultResponse(PLEASE_CONTACT_OUR_TEAM));
    }

    private static Mono<ResponseEntity<ResultResponse>> buildResponse(Result result) {
        if (result.isSuccessful()) return Mono.just(ResponseEntity.ok().build());
        return Mono.just(ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                .body(new ResultResponse(result.message())));
    }
}
