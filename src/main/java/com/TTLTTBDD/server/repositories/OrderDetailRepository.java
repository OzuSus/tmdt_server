package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByIdOder_Id(int orderId);
}