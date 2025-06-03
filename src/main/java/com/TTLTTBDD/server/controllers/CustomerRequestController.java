package com.TTLTTBDD.server.controllers;


import com.TTLTTBDD.server.models.dto.CustomerRequestDTO;
import com.TTLTTBDD.server.services.CustomerRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    @GetMapping("user")
    public ResponseEntity<List<CustomerRequestDTO>> getRequestByUserId(@RequestParam int userId){
        return ResponseEntity.ok(customerRequestService.getRequestByUserId(userId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody CustomerRequestDTO dto) {
        try {
            return ResponseEntity.ok(customerRequestService.createRequest(dto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}

