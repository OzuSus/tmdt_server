package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.JewelerResponseDTORequest;
import com.TTLTTBDD.server.models.dto.JewelerResponseDTOResponse;
import com.TTLTTBDD.server.services.JewelerResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/jeweler-response")
public class JewelerResponseController {
    @Autowired
    private JewelerResponseService jewelerResponseService;

    @GetMapping
    public ResponseEntity<List<JewelerResponseDTOResponse>> getAllResponses() {
        return ResponseEntity.ok(jewelerResponseService.getAllResponses());
    }

    @GetMapping("/request/{requestId}")
    public ResponseEntity<List<JewelerResponseDTOResponse>> getResponsesByRequestId(@PathVariable Integer requestId) {
        return ResponseEntity.ok(jewelerResponseService.getResponsesByRequestId(requestId));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createResponse(
            @RequestParam("customerRequestId") Integer customerRequestId,
            @RequestParam("jewelerId") Integer jewelerId,
            @RequestParam("name") String name,
            @RequestParam("proposedPrice") Double proposedPrice,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Integer categoryId,
            @RequestParam("image") MultipartFile image) {
        try {
            JewelerResponseDTOResponse dto = jewelerResponseService.createResponse(customerRequestId, jewelerId, name, proposedPrice, description, categoryId, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Đã xảy ra lỗi khi xử lý yêu cầu.");
        }
    }


}