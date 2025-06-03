package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.CustomerRequestDTO;
import com.TTLTTBDD.server.models.entity.CustomerRequest;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.repositories.CustomerRequestRepository;
import com.TTLTTBDD.server.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerRequestService {
    @Autowired
    CustomerRequestRepository customerRequestRepository;
    @Autowired
    UserRepository userRepository;

    public List<CustomerRequestDTO> getAllRequests() {
        return customerRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerRequestDTO createRequest(CustomerRequestDTO dto) {
        if (dto.getMinPrice() != null && dto.getMaxPrice() != null && dto.getMinPrice() >= dto.getMaxPrice()) {
            throw new IllegalArgumentException("Giá max phải > giá min");
        }

        CustomerRequest request = new CustomerRequest();
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setMinPrice(dto.getMinPrice());
        request.setMaxPrice(dto.getMaxPrice());
        request.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDate.now());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User ko tồn tại"));
        request.setUser(user);

        CustomerRequest saved = customerRequestRepository.save(request);
        return convertToDTO(saved);
    }


    private CustomerRequestDTO convertToDTO(CustomerRequest request) {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setTitle(request.getTitle());
        dto.setMinPrice(request.getMinPrice());
        dto.setMaxPrice(request.getMaxPrice());
        dto.setDescription(request.getDescription());
        dto.setCreatedAt(request.getCreatedAt());
        return dto;
    }
}
