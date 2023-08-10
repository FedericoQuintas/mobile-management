package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.requests.ReturnPhoneRequest;
import com.challenge.mobilemanagement.api.responses.PhoneEventResponse;
import com.challenge.mobilemanagement.api.responses.ResultResponse;
import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.usecases.PhonesQueryService;
import com.challenge.mobilemanagement.usecases.returnPhone.ReturnPhoneCommand;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class FetchPhoneHistoryController {

    private final PhonesQueryService phonesQueryService;

    public FetchPhoneHistoryController(PhonesQueryService phonesQueryService) {
        this.phonesQueryService = phonesQueryService;
    }

    @GetMapping("/phones/{model}/history")
    public Flux<PhoneEventResponse> history(@PathVariable String model) {
        return phonesQueryService.fetchHistory(PhoneModel.of(model)).map(PhoneEventResponse::from);
    }
}
