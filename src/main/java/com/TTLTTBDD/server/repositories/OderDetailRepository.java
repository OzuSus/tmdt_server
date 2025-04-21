package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.OderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OderDetailRepository extends JpaRepository<OderDetail, Integer> {
    List<OderDetail> findByIdOder_Id(int orderId);
}