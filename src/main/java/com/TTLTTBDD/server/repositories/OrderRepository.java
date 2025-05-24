package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByIdUser_Id(int userId);
    List<Order> findByIdStatus_Id(int statusId);
    Optional<Order> findTopByOrderByIdDesc();

    List<Order> findByIdStatus_IdAndIdUser_Id(Integer statusId, Integer userId);
}