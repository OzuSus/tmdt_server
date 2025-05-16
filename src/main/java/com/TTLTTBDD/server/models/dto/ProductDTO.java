package com.TTLTTBDD.server.models.dto;

import com.TTLTTBDD.server.models.entity.Category;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer id;
    private String name;
    private Double price;
    private BigDecimal quantity;
    private String image;
    private String description;
    private Integer reviewCount;
    private Double rating;
    private Integer categoryID;
    private String tag;
    private Integer jewelerID;
}
