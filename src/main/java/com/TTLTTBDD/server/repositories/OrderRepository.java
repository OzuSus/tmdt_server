package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByIdUser_Id(int userId);
    List<Order> findByIdStatus_Id(int statusId);
    Optional<Order> findTopByOrderByIdDesc();

    List<Order> findByIdStatus_IdAndIdUser_Id(Integer statusId, Integer userId);

    @Query("SELECT MONTH(o.dateOrder) AS month, SUM(o.totalPrice) AS revenue " +
            "FROM Order o " +
            "WHERE YEAR(o.dateOrder) = :year AND o.idStatus.id = 8" +
            "GROUP BY MONTH(o.dateOrder) " +
            "ORDER BY MONTH(o.dateOrder)")
    List<Object[]> getRevenuePerMonth(@Param("year") int year);
}