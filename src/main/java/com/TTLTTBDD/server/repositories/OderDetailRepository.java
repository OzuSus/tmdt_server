package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByIdOder_Id(int orderId);

    @Query("SELECT COUNT(od) > 0 FROM OrderDetail od " +
            "JOIN od.idOder o " +
            "WHERE o.idUser.id = :userId AND od.idProduct.id = :productId " +
            "AND o.idStatus.name = 'COMPLETED'")
    boolean hasUserPurchasedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);
}