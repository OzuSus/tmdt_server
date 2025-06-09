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
public class JewelerResponseDTORequest {
    private Integer id;
    private Integer customerRequestId;
    private Integer jewelerId;
    private String name;
    private Double proposedPrice;
    private String description;
    private String image;
    private LocalDate createdAt;
    private Integer categoryId;

}
