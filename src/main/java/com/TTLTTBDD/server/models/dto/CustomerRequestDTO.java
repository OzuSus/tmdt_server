package com.TTLTTBDD.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTO {
    private Integer id;
    private Integer userId;
    private String title;
    private Double minPrice;
    private Double maxPrice;
    private String description;
    private LocalDate createdAt;
    private Integer categoryId;

}
