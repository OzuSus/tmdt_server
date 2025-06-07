package com.TTLTTBDD.server.repositories;

import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Optional<Product> findProductById(int id);
    List<Product> findByIdCategory_Id(int idCategory);
    List<Product> findByTagIgnoreCase(String tag);
    List<Product> findByRating(Double rating);
    List<Product> findByPrizeBetween(double min, double max);

    Optional<Product> findByNameAndDescriptionAndImageAndIdCategory(
            String name, String description, String image, Category idCategory);



}

