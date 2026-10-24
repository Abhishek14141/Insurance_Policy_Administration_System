package org.example.insurance_policy_system.client;

import org.example.insurance_policy_system.dto.undertakingDto.QuoteResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "quote-service")
public interface QuoteClient {

    @GetMapping("/api/quotes/{id}")
    QuoteResponse getQuoteById(@PathVariable("id") Long quoteId) ;

}
