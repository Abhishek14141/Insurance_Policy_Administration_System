package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.QuoteRider;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QuoteRiderRepository extends JpaRepository<QuoteRider, Long> {

    void deleteByQuote_QuoteId(Long quoteId);

}
