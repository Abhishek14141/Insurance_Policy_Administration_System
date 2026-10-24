package org.example.insurance_policy_system.repository;
import org.example.insurance_policy_system.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
public interface QuoteRepository extends JpaRepository<Quote, Long> {

}
