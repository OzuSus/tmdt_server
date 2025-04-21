package com.TTLTTBDD.server.models.dto;

import com.TTLTTBDD.server.models.entity.OderDetail;
import com.TTLTTBDD.server.models.entity.PaymentMethop;
import com.TTLTTBDD.server.models.entity.Status;
import com.TTLTTBDD.server.models.entity.User;
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
    private String paymentMethodName;
    private StatusDTO statusName;
    private List<OderDetailDTO> orderDetails;
}
