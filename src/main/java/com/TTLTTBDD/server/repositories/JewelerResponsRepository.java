package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.JewelerRespons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JewelerResponsRepository extends JpaRepository<JewelerRespons, Integer> {
    List<JewelerRespons> findAllByRequest_Id(Integer requestId);
    boolean existsByJeweler_IdAndRequest_Id(Integer jewelerId, Integer requestId);
    void deleteByRequest_Id(int requestId);
}

