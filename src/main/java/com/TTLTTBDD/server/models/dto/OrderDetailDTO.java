package com.TTLTTBDD.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDTO {
    private Integer id;
//    private Integer idOder;
    private ProductDTO Product;
    private Integer quantity;
    private Double price;

}
