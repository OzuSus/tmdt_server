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
public class JewelerResponseDTOResponse {
    private Integer id;
    private CustomerRequestDTOResponse customerRequest;
    private UserDTO jeweler;
    private String name;
    private Double proposedPrice;
    private String description;
    private String image;
    private LocalDate createdAt;
    private CategoryDTO category;
}
