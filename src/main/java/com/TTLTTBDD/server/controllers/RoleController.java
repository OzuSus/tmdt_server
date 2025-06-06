package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.RoleDTO;
import com.TTLTTBDD.server.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
    return ResponseEntity.ok(roleService.getAllRole());
}
}
