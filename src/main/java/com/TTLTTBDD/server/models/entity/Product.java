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

    @Column(name = "review")
    private Integer review;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "prize", nullable = false)
    private Double prize;

    @Column(name = "quantity", nullable = false, precision = 10)
    private BigDecimal quantity;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "description", nullable = false, length = 100)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_category", nullable = false)
    private Category idCategory;

    @Column(name = "tag", length = 50)
    private String tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_jeweler", referencedColumnName = "id_user")
    private User idJeweler;

}