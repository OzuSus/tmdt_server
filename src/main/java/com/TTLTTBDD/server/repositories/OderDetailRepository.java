package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.OderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OderDetailRepository extends JpaRepository<OderDetail, Integer> {
    List<OderDetail> findByIdOder_Id(int orderId);

    @Query("SELECT COUNT(od) > 0 FROM OderDetail od " +
            "JOIN od.idOder o " +
            "WHERE o.idUser.id = :userId AND od.idProduct.id = :productId " +
            "AND o.idStatus.name = 'COMPLETED'")
    boolean hasUserPurchasedProduct(@Param("userId") Integer userId, @Param("productId") Integer productId);
}