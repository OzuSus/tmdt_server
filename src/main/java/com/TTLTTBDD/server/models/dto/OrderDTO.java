package com.TTLTTBDD.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private int idOrder;
    private int userId;
    private LocalDate dateOrder;
    private PaymentMethodDTO paymentMethod;
    private StatusDTO status;
    private String fullname;
    private String email;
    private String phone;
    private String address;
    private List<OrderDetailDTO> orderDetails;
    private Double totalPrice;
    private DeliveryMethopDTO deliveryMethop;

}
