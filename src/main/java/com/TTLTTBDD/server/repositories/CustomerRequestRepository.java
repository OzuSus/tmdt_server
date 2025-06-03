package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.CustomerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Integer> {
}
