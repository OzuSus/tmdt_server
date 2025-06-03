package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.CustomerRequest;
import com.TTLTTBDD.server.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRequestRepository extends JpaRepository<CustomerRequest, Integer> {
    List<CustomerRequest> findByUser_Id(int userId);

}
