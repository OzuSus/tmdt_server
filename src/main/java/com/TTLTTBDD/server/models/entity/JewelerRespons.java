package com.TTLTTBDD.server.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "jeweler_responses")
public class JewelerRespons {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_response", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "requestId", nullable = false)
    private CustomerRequest request;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "jewelerId", nullable = false, referencedColumnName = "id_user")
    private User jeweler;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "proposed_price", nullable = false)
    private Double proposedPrice;

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "image", nullable = false)
    private String image;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

}