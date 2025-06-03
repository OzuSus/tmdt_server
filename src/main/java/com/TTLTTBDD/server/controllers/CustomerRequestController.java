package com.TTLTTBDD.server.controllers;


import com.TTLTTBDD.server.models.dto.CustomerRequestDTO;
import com.TTLTTBDD.server.services.CustomerRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/customer-requests")
public class CustomerRequestController {
    @Autowired
    private CustomerRequestService customerRequestService;

    @GetMapping
    public ResponseEntity<List<CustomerRequestDTO>> getAllRequests() {
        return ResponseEntity.ok(customerRequestService.getAllRequests());
    }

    @PostMapping
    public ResponseEntity<CustomerRequestDTO> createRequest(@RequestBody CustomerRequestDTO dto) {
        return ResponseEntity.ok(customerRequestService.createRequest(dto));
    }
}
