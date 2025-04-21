package com.TTLTTBDD.server.models.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "payment_methops")
public class PaymentMethop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment_methop", nullable = false)
    private Integer id;

    @Column(name = "type_payment", nullable = false, length = 50)
    private String typePayment;

}