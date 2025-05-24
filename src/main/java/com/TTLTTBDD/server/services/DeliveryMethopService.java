package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.DeliveryMethopDTO;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.entity.DeliveryMethop;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.repositories.DeliveryMethopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DeliveryMethopService {
    @Autowired
    DeliveryMethopRepository deliveryMethopRepository;

    public List<DeliveryMethopDTO> getAllDeliveryMethop() {
        return deliveryMethopRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private DeliveryMethopDTO convertToDTO(DeliveryMethop deliveryMethop) {
        return DeliveryMethopDTO.builder()
                .id(deliveryMethop.getId())
                .name(deliveryMethop.getName())
                .price(deliveryMethop.getPrice())
                .description(deliveryMethop.getDescription())
                .build();
    }
}
