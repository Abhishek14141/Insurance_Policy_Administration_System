package org.example.insurance_policy_system.repository.quote;

import com.springproject.insurance_policy_system.entity.quote.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
}
