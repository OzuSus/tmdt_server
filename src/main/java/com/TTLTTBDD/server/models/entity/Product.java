package com.TTLTTBDD.server.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product", nullable = false)
    private Integer id;

    @Column(name = "review", nullable = true)
    private Integer review;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "prize", nullable = false)
    private Double prize;

    @Column(name = "quantity", nullable = false, precision = 10)
    private BigDecimal quantity;

    @Column(name = "rating", nullable = true)
    private Double rating;

    @Column(name = "image", nullable = false, length = 50)
    private String image;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_category", nullable = false)
    private Category idCategory;

    @Column(name = "tag", length = 50)
    private String tag;

}