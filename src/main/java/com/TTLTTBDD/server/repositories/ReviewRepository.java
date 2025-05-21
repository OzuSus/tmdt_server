package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProductId(Integer productId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.user.id = :userId AND r.product.id = :productId")
    boolean hasUserReviewedProduct(@Param("userId") Integer userId,
                                   @Param("productId") Integer productId);
}