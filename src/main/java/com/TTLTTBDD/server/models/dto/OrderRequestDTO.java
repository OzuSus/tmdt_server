package com.TTLTTBDD.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Integer idUser;
    private Integer idPaymentMethop;
    private List<ProductOrderDTO> products;
}
