package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Favorite;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findAllByIdUser(User user);
    void deleteByIdUserAndIdProduct(User user, Product product);
    boolean existsByIdUserAndIdProduct(User user, Product product);
    void deleteAllByIdUser(User user);
}