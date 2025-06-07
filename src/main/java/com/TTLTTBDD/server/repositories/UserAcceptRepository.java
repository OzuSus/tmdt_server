package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.UserAccept;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAcceptRepository extends JpaRepository<UserAccept, Integer> {
    void deleteByRequest_Id(int requestId);
}
