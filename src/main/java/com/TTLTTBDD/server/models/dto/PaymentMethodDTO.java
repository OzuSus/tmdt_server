package com.TTLTTBDD.server.models.dto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentMethodDTO {
    private Integer id;
    private String type_payment;
}
