package org.example.insurance_policy_system.repository.insuranceManagement;

import org.example.insurance_policy_system.entity.insuranceManagement.Rider;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiderRepository extends JpaRepository<Rider,Long> { List<Rider> findByProduct_ProductId(Long productId); }
