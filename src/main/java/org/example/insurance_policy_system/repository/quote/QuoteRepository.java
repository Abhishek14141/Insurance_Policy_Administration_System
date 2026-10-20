package org.example.insurance_policy_system.repository.quote;

import com.springproject.insurance_policy_system.entity.quote.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {
}
