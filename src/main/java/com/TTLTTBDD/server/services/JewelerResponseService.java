package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.exception.DuplicateJewelerResponseException;
import com.TTLTTBDD.server.models.dto.*;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.CustomerRequest;
import com.TTLTTBDD.server.models.entity.JewelerRespons;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.repositories.CategotyRepository;
import com.TTLTTBDD.server.repositories.CustomerRequestRepository;
import com.TTLTTBDD.server.repositories.JewelerResponsRepository;
import com.TTLTTBDD.server.repositories.UserRepository;
import com.TTLTTBDD.server.utils.loadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JewelerResponseService {
    private loadFile fileUploader = new loadFile();

    @Autowired
    private JewelerResponsRepository jewelerResponsRepository;
    @Autowired
    private CustomerRequestRepository customerRequestRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategotyRepository categotyRepository;


    public List<JewelerResponseDTOResponse> getAllResponses() {
        return jewelerResponsRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<JewelerResponseDTOResponse> getResponsesByRequestId(Integer requestId) {
        return jewelerResponsRepository.findAllByRequest_Id(requestId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public JewelerResponseDTOResponse createResponse(Integer customerRequestId, Integer jewelerId, String name, Double proposedPrice, String description, Integer categoryId, MultipartFile image) throws IOException {
        boolean alreadyResponded = jewelerResponsRepository.existsByJeweler_IdAndRequest_Id(jewelerId, customerRequestId);
        if (alreadyResponded) {
            throw new DuplicateJewelerResponseException("Thợ kim hoàn đã gửi phản hồi cho yêu cầu này.");
        }

        CustomerRequest request = customerRequestRepository.findById(customerRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu khách hàng"));
        User jeweler = userRepository.findById(jewelerId)
                .filter(u -> u.getRole().getId() == 1)
                .orElseThrow(() -> new IllegalArgumentException("Mã thợ kim hoàn ko hợp lệ"));

        if (proposedPrice < request.getMinPrice() || proposedPrice > request.getMaxPrice()) {
            throw new IllegalArgumentException("Giá đề xuất phải nằm trong khoảng từ " + request.getMinPrice() + " đến " + request.getMaxPrice());
        }

        Category category = categotyRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Danh mục ko hợp lệ"));

        String fileName = fileUploader.saveFile(image);

        JewelerRespons entity = new JewelerRespons();
        entity.setRequest(request);
        entity.setJeweler(jeweler);
        entity.setName(name);
        entity.setProposedPrice(proposedPrice);
        entity.setDescription(description);
        entity.setImage(fileName);
        entity.setCreatedAt(LocalDate.now());
        entity.setCategory(category);

        return toDTO(jewelerResponsRepository.save(entity));
    }

    private JewelerResponseDTOResponse toDTO(JewelerRespons jewelerRespons) {
        JewelerResponseDTOResponse dtoResponse = new JewelerResponseDTOResponse();

        UserDTO userDTO = null;
        if(jewelerRespons.getRequest().getUser() != null){
            userDTO = UserDTO.builder()
                    .id(jewelerRespons.getRequest().getUser().getId())
                    .username(jewelerRespons.getRequest().getUser().getUsername())
                    .fullname(jewelerRespons.getRequest().getUser().getFullname())
                    .address(jewelerRespons.getRequest().getUser().getAddress())
                    .phone(jewelerRespons.getRequest().getUser().getPhone())
                    .email(jewelerRespons.getRequest().getUser().getEmail())
                    .roleId(jewelerRespons.getRequest().getUser().getId())
                    .avatar(jewelerRespons.getRequest().getUser().getAvatar())
                    .status(jewelerRespons.getRequest().getUser().getStatus())
                    .build();
        }
        CategoryDTO categoryDTO = null;
        if (jewelerRespons.getRequest().getIdcategory() != null) {
            categoryDTO = CategoryDTO.builder()
                    .id(jewelerRespons.getRequest().getIdcategory().getId())
                    .name(jewelerRespons.getRequest().getIdcategory().getName())
                    .image(jewelerRespons.getRequest().getIdcategory().getImage())
                    .build();
        }
        CustomerRequestDTOResponse customerRequest = null;
        if(jewelerRespons.getRequest() != null){
            customerRequest = CustomerRequestDTOResponse.builder()
                    .id(jewelerRespons.getRequest().getId())
                    .user(userDTO)
                    .title(jewelerRespons.getRequest().getTitle())
                    .minPrice(jewelerRespons.getRequest().getMinPrice())
                    .maxPrice(jewelerRespons.getRequest().getMaxPrice())
                    .description(jewelerRespons.getDescription())
                    .createdAt(jewelerRespons.getRequest().getCreatedAt())
                    .category(categoryDTO)
                    .build();
        }

        UserDTO jeweler = null;
        if(jewelerRespons.getJeweler() != null){
            jeweler = UserDTO.builder()
                    .id(jewelerRespons.getJeweler().getId())
                    .username(jewelerRespons.getJeweler().getUsername())
                    .fullname(jewelerRespons.getJeweler().getFullname())
                    .address(jewelerRespons.getJeweler().getAddress())
                    .phone(jewelerRespons.getJeweler().getPhone())
                    .email(jewelerRespons.getJeweler().getEmail())
                    .roleId(jewelerRespons.getJeweler().getId())
                    .avatar(jewelerRespons.getJeweler().getAvatar())
                    .status(jewelerRespons.getJeweler().getStatus())
                    .build();
        }
        CategoryDTO category = null;
        if (jewelerRespons.getCategory() != null) {
            category = CategoryDTO.builder()
                    .id(jewelerRespons.getCategory().getId())
                    .name(jewelerRespons.getCategory().getName())
                    .image(jewelerRespons.getCategory().getImage())
                    .build();
        }
        dtoResponse.setId(jewelerRespons.getId());
        dtoResponse.setCustomerRequest(customerRequest);
        dtoResponse.setJeweler(jeweler);
        dtoResponse.setName(jewelerRespons.getName());
        dtoResponse.setName(jewelerRespons.getName());
        dtoResponse.setProposedPrice(jewelerRespons.getProposedPrice());
        dtoResponse.setDescription(jewelerRespons.getDescription());
        dtoResponse.setImage(jewelerRespons.getImage());
        dtoResponse.setCreatedAt(jewelerRespons.getCreatedAt());
        dtoResponse.setCategory(category);
        return dtoResponse;
    }
}
