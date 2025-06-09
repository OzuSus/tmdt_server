package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.CategoryDTO;
import com.TTLTTBDD.server.models.dto.CustomerRequestDTORequest;
import com.TTLTTBDD.server.models.dto.CustomerRequestDTOResponse;
import com.TTLTTBDD.server.models.dto.UserDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.CustomerRequest;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.models.entity.UserAccept;
import com.TTLTTBDD.server.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerRequestService {
    @Autowired
    CustomerRequestRepository customerRequestRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    CategotyRepository categotyRepository;
    @Autowired
    private JewelerResponsRepository jewelerResponsRepository;
    @Autowired
    private UserAcceptRepository userAcceptRepository;
    public List<CustomerRequestDTOResponse> getAllRequests() {
        return customerRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<CustomerRequestDTOResponse> getRequestByUserId(int userId) {
        return customerRequestRepository.findByUser_Id(userId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    public List<CustomerRequestDTOResponse> getRequestById(int Id) {
        return customerRequestRepository.findById(Id).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerRequestDTOResponse createRequest(CustomerRequestDTORequest dtoRequest) {
        User user = userRepository.findById(dtoRequest.getUserId())
                .filter(u -> u.getRole().getId() == 0)
                .orElseThrow(() -> new RuntimeException("Lỗi: User ko tồn tại or user là jeweler/admin"));
        if (dtoRequest.getMinPrice() == null || dtoRequest.getMaxPrice() == null) {
            throw new IllegalArgumentException("Giá min/max không được để trống");
        }
        if (dtoRequest.getMinPrice() > dtoRequest.getMaxPrice()) {
            throw new IllegalArgumentException("Giá min phải <= giá max");
        }

        Category category = categotyRepository.findCategoryById(dtoRequest.getCategoryId()).orElseThrow(() -> new IllegalArgumentException("Category khong hop le"));

        CustomerRequest request = new CustomerRequest();
        request.setTitle(dtoRequest.getTitle());
        request.setDescription(dtoRequest.getDescription());
        request.setMinPrice(dtoRequest.getMinPrice());
        request.setMaxPrice(dtoRequest.getMaxPrice());
        request.setCreatedAt(dtoRequest.getCreatedAt() != null ? dtoRequest.getCreatedAt() : LocalDate.now());
        request.setIdcategory(category);
        request.setUser(user);

        CustomerRequest saved = customerRequestRepository.save(request);
        return convertToDTO(saved);
    }


    public void deleteRequestById(int id, int userId) throws Exception {
        Optional<CustomerRequest> optionalRequest = customerRequestRepository.findById(id);

        if (optionalRequest.isEmpty()) {
            throw new Exception("Request không tồn tại");
        }
        CustomerRequest request = optionalRequest.get();
        if (request.getUser() == null || !request.getUser().getId().equals(userId)) {
            throw new Exception("Bạn không có quyền xóa request này");
        }
//        Optional<UserAccept> optionalUserAcept = userAcceptRepository.findById(id);
        userAcceptRepository.deleteByRequest_Id(id);
        jewelerResponsRepository.deleteByRequest_Id(id);
        customerRequestRepository.deleteById(id);
    }



    private CustomerRequestDTOResponse convertToDTO(CustomerRequest request) {
        CustomerRequestDTOResponse dtoResponse = new CustomerRequestDTOResponse();
        CategoryDTO categoryDTO = null;
        if (request.getIdcategory() != null) {
            categoryDTO = CategoryDTO.builder()
                    .id(request.getIdcategory().getId())
                    .name(request.getIdcategory().getName())
                    .image(request.getIdcategory().getImage())
                    .build();
        }
        UserDTO userDTO = null;
        if(request.getUser() != null){
            userDTO = UserDTO.builder()
                    .id(request.getUser().getId())
                    .username(request.getUser().getUsername())
                    .fullname(request.getUser().getFullname())
                    .address(request.getUser().getAddress())
                    .phone(request.getUser().getPhone())
                    .email(request.getUser().getEmail())
                    .roleId(request.getUser().getRole().getId())
                    .avatar(request.getUser().getAvatar())
                    .status(request.getUser().getStatus())
                    .build();
        }
        dtoResponse.setId(request.getId());
        dtoResponse.setUser(userDTO);
        dtoResponse.setTitle(request.getTitle());
        dtoResponse.setMinPrice(request.getMinPrice());
        dtoResponse.setMaxPrice(request.getMaxPrice());
        dtoResponse.setDescription(request.getDescription());
        dtoResponse.setCreatedAt(request.getCreatedAt());
        dtoResponse.setCategory(categoryDTO);
        return dtoResponse;
    }
}
