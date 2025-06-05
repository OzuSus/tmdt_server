package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.CategoryDTO;
import com.TTLTTBDD.server.models.dto.CustomerRequestDTO;
import com.TTLTTBDD.server.models.dto.JewelerResponseDTO;
import com.TTLTTBDD.server.models.dto.UserDTO;
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


    public List<JewelerResponseDTO> getAllResponses() {
        return jewelerResponsRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<JewelerResponseDTO> getResponsesByRequestId(Integer requestId) {
        return jewelerResponsRepository.findAllByRequest_Id(requestId).stream().map(this::toDTO).collect(Collectors.toList());
    }

    public JewelerResponseDTO createResponse(Integer customerRequestId, Integer jewelerId, String name, Double proposedPrice, String description, Integer categoryId, MultipartFile image) throws IOException {

        CustomerRequest request = customerRequestRepository.findById(customerRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy yêu cầu khách hàng"));

        User jeweler = userRepository.findById(jewelerId)
                .filter(u -> u.getRole().getId() == 1)
                .orElseThrow(() -> new IllegalArgumentException("Mã thợ kim hoàn ko hợp lệ"));


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

    private JewelerResponseDTO toDTO(JewelerRespons jewelerRespons) {
        return JewelerResponseDTO.builder()
                .id(jewelerRespons.getId())
                .customerRequestId(jewelerRespons.getRequest().getId())
                .jewelerId(jewelerRespons.getJeweler().getId())
                .name(jewelerRespons.getName())
                .proposedPrice(jewelerRespons.getProposedPrice())
                .description(jewelerRespons.getDescription())
                .image(jewelerRespons.getImage())
                .createdAt(jewelerRespons.getCreatedAt())
                .categoryId(jewelerRespons.getCategory().getId())
                .build();
    }
}
