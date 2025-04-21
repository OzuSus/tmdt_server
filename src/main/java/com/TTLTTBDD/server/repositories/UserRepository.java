package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsernameAndPassword(String username, String password);
    Optional<User> findUsersById(int id);
    Optional<User> findByUsernameAndEmail(String username, String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);

}
