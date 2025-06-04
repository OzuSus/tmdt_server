package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.JewelerResponseDTO;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/jeweler-response")
public class JewelerResponseController {

//    @GetMapping
//    public List<JewelerResponseDTO> getResponseOfRequest(int requestId){
//
//    }
}
