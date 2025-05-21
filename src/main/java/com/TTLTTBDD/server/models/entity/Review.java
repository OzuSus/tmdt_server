package com.TTLTTBDD.server.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_review")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id_user", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", referencedColumnName = "id_product", nullable = false)
    private Product product;

    @Column(name = "comment", columnDefinition = "TINYTEXT", nullable = false)
    private String comment;

    @Column(name = "rating_star", nullable = false)
    private Integer ratingStar;

    @Column(name = "date", nullable = false)
    private LocalDate date;
}