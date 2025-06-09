package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.CustomerRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Integer> {
    List<CustomerRequest> findByUser_Id(int userId);


}
