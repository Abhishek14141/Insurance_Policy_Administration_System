package org.example.insurance_policy_system.repository.quote;


import org.example.insurance_policy_system.entity.quote.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
