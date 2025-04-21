package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Oder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import java.util.Optional;

public interface OderRepository extends JpaRepository<Oder, Integer> {
    List<Oder> findByIdUser_Id(int userId);

    Optional<Oder> findTopByOrderByIdDesc(); // Lấy đơn hàng có ID lớn nhất
}