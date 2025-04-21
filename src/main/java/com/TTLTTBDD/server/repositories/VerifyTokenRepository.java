package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Verifytoken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerifyTokenRepository extends JpaRepository<Verifytoken, Long> {
    Optional<Verifytoken> findByToken(String token);

}
