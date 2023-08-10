package com.challenge.mobilemanagement.api;

import com.challenge.mobilemanagement.api.responses.PhoneEventResponse;
import com.challenge.mobilemanagement.api.responses.PhoneStatusResponse;
import com.challenge.mobilemanagement.domain.PhoneModel;
import com.challenge.mobilemanagement.usecases.query.PhonesQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PhonesController {

    private final PhonesQueryService phonesQueryService;

    public PhonesController(PhonesQueryService phonesQueryService) {
        this.phonesQueryService = phonesQueryService;
    }

    @GetMapping("/phones/{model}/history")
    public Flux<PhoneEventResponse> history(@PathVariable String model) {
        return phonesQueryService.fetchHistory(PhoneModel.of(model)).map(PhoneEventResponse::from);
    }

    @GetMapping("/phones")
    public Flux<PhoneStatusResponse> status() {
        return phonesQueryService.fetchAll().map(PhoneStatusResponse::from);
    }
}
