package com.TTLTTBDD.server.models.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewResponseDTO {
    private Integer id;
    private String comment;
    private int rating;
    private String userName;
    //    private String productName;
    private LocalDate createdAt;
}