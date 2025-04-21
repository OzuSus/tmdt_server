package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Cart;
import com.TTLTTBDD.server.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByIdUser_Id(Integer idUser);
}
