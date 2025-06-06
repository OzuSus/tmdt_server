package com.TTLTTBDD.server.services;


import com.TTLTTBDD.server.models.dto.RoleDTO;
import com.TTLTTBDD.server.models.entity.Role;
import com.TTLTTBDD.server.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;
    public List<RoleDTO> getAllRole() {

        return roleRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    public RoleDTO convertToDTO(Role role) {
        return RoleDTO.builder()
                .roleId(role.getId())
                .roleName(role.getNameRole())
                .build();
    }
}
