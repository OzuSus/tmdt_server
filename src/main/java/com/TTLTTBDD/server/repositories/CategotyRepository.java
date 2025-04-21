package com.TTLTTBDD.server.repositories;

import aj.org.objectweb.asm.commons.Remapper;
import com.TTLTTBDD.server.models.dto.CategoryDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategotyRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findCategoryById(int id);
}
