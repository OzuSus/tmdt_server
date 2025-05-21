package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.OrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByIdOder_Id(int orderId);

    @Query("SELECT CASE WHEN COUNT(od) > 0 THEN true ELSE false END " +
            "FROM OrderDetail od " +
            "JOIN od.idOder o " +
            "WHERE o.idUser.id = :userId AND od.idProduct.id = :productId")
    boolean hasUserPurchasedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);

}