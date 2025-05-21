package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategotyRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryById(int id);
}
