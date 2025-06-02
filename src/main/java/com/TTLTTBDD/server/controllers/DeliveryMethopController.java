package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.DeliveryMethopDTO;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.services.DeliveryMethopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/deliveryMethop")
public class DeliveryMethopController {

    @Autowired
    DeliveryMethopService deliveryMethopService;

    @GetMapping
    public List<DeliveryMethopDTO> getAllProducts() {
        return deliveryMethopService.getAllDeliveryMethop();
    }

}
