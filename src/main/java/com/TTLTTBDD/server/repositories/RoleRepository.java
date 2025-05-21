package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Cart;
import com.TTLTTBDD.server.models.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Integer> {
    Optional<Role> findById(Integer idRole);
}
