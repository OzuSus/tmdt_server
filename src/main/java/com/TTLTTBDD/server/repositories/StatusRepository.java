package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {

}