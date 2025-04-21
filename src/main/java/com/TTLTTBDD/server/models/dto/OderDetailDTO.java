package com.TTLTTBDD.server.models.dto;

import com.TTLTTBDD.server.models.entity.Oder;
import com.TTLTTBDD.server.models.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OderDetailDTO {
    private Integer id;
    private Integer idOder;
    private ProductDTO idProduct;
    private Integer quantity;
    private Double totalprice;

}
