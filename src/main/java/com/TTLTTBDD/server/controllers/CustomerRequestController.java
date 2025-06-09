package com.TTLTTBDD.server.controllers;


import com.TTLTTBDD.server.models.dto.CustomerRequestDTORequest;
import com.TTLTTBDD.server.models.dto.CustomerRequestDTOResponse;
import com.TTLTTBDD.server.services.CustomerRequestService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<CustomerRequestDTOResponse>> getAllRequests() {
        return ResponseEntity.ok(customerRequestService.getAllRequests());
    }
    @GetMapping("user")
    public ResponseEntity<List<CustomerRequestDTOResponse>> getRequestByUserId(@RequestParam int userId){
        return ResponseEntity.ok(customerRequestService.getRequestByUserId(userId));
    }
    @GetMapping("id")
    public ResponseEntity<List<CustomerRequestDTOResponse>> getRequestById(@RequestParam int id){
        return ResponseEntity.ok(customerRequestService.getRequestById(id));
    }
    @PostMapping("/create")
    public ResponseEntity<?> createRequest(@RequestBody CustomerRequestDTORequest dtoRequest) {
        try {
            return ResponseEntity.ok(customerRequestService.createRequest(dtoRequest));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Transactional
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteRequest(@RequestParam int id, @RequestParam int userId) {
        try {
            customerRequestService.deleteRequestById(id, userId);
            return ResponseEntity.ok("Xóa yêu cầu và phản hồi thành công.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }



}

