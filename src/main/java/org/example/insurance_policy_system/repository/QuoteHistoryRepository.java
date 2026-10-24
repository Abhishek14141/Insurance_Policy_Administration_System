package org.example.insurance_policy_system.repository;

import org.example.insurance_policy_system.entity.QuoteHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuoteHistoryRepository extends JpaRepository<QuoteHistory, Long> {
    List<QuoteHistory> findByQuote_QuoteIdOrderByActionDateDesc(Long quoteId);
}
