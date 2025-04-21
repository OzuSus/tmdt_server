package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Cart;
import com.TTLTTBDD.server.models.entity.CartDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartDetailRepository extends JpaRepository<CartDetail, Integer> {
    Optional<CartDetail> findByIdCart_IdAndIdProduct_Id(Integer idCart, Integer idProduct);
    List<CartDetail> findAllByIdCart_Id(Integer idCart);
    void deleteAllByIdCart(Cart idCart);
}
