package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.UserAcceptDTO;
import com.TTLTTBDD.server.models.entity.UserAccept;
import com.TTLTTBDD.server.services.UserAcceptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-accepts")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserAcceptController {

    @Autowired
    private UserAcceptService userAcceptService;

    @PostMapping("/add")
    public ResponseEntity<?> addToAccept(@RequestParam Integer requestId, @RequestParam Integer responseId) {
        try {
            UserAcceptDTO result = userAcceptService.addToAccept(requestId, responseId);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
