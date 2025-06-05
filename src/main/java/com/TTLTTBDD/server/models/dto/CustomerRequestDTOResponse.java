package com.TTLTTBDD.server.models.dto;

import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerRequestDTOResponse {
    private Integer id;
    private UserDTO user;
    private String title;
    private Double minPrice;
    private Double maxPrice;
    private String description;
    private LocalDate createdAt;
    private CategoryDTO category;
}
