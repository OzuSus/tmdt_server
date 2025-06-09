package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAcceptRepository extends JpaRepository<UserAccept, Integer> {
    void deleteByRequest_Id(int requestId);
    boolean existsByRequest_IdAndResponse_Id(int requestId, int responseId);

}
