package com.TTLTTBDD.server.models.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewRequestDTO {
    private Integer userId;
    private Integer productId;
    private String comment;
    private Integer rating;
}