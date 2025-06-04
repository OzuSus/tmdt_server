package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.CategoryDTO;
import com.TTLTTBDD.server.models.dto.CustomerRequestDTO;
import com.TTLTTBDD.server.models.dto.DeliveryMethopDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.CustomerRequest;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.repositories.CategotyRepository;
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
    @Autowired
    CategotyRepository categotyRepository;

    public List<CustomerRequestDTO> getAllRequests() {
        return customerRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<CustomerRequestDTO> getRequestByUserId(int userId) {
        return customerRequestRepository.findByUser_Id(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<CustomerRequestDTO> getRequestById(int Id) {
        return customerRequestRepository.findById(Id).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerRequestDTO createRequest(CustomerRequestDTO dto) {
        if (dto.getMinPrice() != null && dto.getMaxPrice() != null && dto.getMinPrice() >= dto.getMaxPrice()) {
            throw new IllegalArgumentException("Giá max phải > giá min");
        }
        Category category = categotyRepository.findCategoryById(dto.getCategoryId().getId()).orElseThrow(() -> new IllegalArgumentException("Category khong hop le"));

        CustomerRequest request = new CustomerRequest();
        request.setTitle(dto.getTitle());
        request.setDescription(dto.getDescription());
        request.setMinPrice(dto.getMinPrice());
        request.setMaxPrice(dto.getMaxPrice());
        request.setCreatedAt(dto.getCreatedAt() != null ? dto.getCreatedAt() : LocalDate.now());
        request.setIdcategory(category);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User ko tồn tại"));
        request.setUser(user);

        CustomerRequest saved = customerRequestRepository.save(request);
        return convertToDTO(saved);
    }


    private CustomerRequestDTO convertToDTO(CustomerRequest request) {
        CustomerRequestDTO dto = new CustomerRequestDTO();
        CategoryDTO categoryDTO = null;
        if (request.getIdcategory() != null) {
            categoryDTO = CategoryDTO.builder()
                    .id(request.getIdcategory().getId())
                    .name(request.getIdcategory().getName())
                    .image(request.getIdcategory().getImage())
                    .build();
        }
        dto.setId(request.getId());
        dto.setUserId(request.getUser().getId());
        dto.setTitle(request.getTitle());
        dto.setMinPrice(request.getMinPrice());
        dto.setMaxPrice(request.getMaxPrice());
        dto.setDescription(request.getDescription());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setCategoryId(categoryDTO);
        return dto;
    }
}
