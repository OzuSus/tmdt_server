package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.CartDetailDTO;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.dto.UserAcceptDTO;
import com.TTLTTBDD.server.models.entity.*;
import com.TTLTTBDD.server.repositories.CustomerRequestRepository;
import com.TTLTTBDD.server.repositories.JewelerResponsRepository;
import com.TTLTTBDD.server.repositories.ProductRepository;
import com.TTLTTBDD.server.repositories.UserAcceptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class UserAcceptService {

    @Autowired
    private UserAcceptRepository userAcceptRepository;
    @Autowired
    private CustomerRequestRepository customerRequestRepository;
    @Autowired
    private JewelerResponsRepository jewelerResponsRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private CartDetailService cartDetailService;

    public UserAcceptDTO addToAccept(Integer requestId, Integer responseId) {
        CustomerRequest request = customerRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Ko tìm thấy request!"));

        JewelerRespons response = jewelerResponsRepository.findById(responseId)
                .orElseThrow(() -> new RuntimeException("Ko tìm thấy response"));

        String name = response.getName();
        BigDecimal quantity = BigDecimal.valueOf(100);
        Double prize = response.getProposedPrice();
        String description = response.getDescription();
        Category category = response.getCategory();
        String image = response.getImage();
        User user = response.getJeweler();
        Optional<Product> existingProductOpt = productRepository.findByNameAndDescriptionAndImageAndIdCategory(name, description, image, category);
        ProductDTO newProduct = null;
        if (existingProductOpt.isPresent()) {
            newProduct = productService.convertToDTO(existingProductOpt.get());
        } else {
            newProduct = productService.addProductWithIdJeweler(name, quantity, prize, description, category, image, user);
        }

        Integer idUser = request.getUser().getId();
        Integer idProduct = newProduct.getId();
        CartDetailDTO cartDetailDTO = cartDetailService.addProductToCartDetail(idUser, idProduct);

        UserAccept userAccept = new UserAccept();
        userAccept.setRequest(request);
        userAccept.setResponse(response);

        UserAccept saved = userAcceptRepository.save(userAccept);

        UserAcceptDTO dto = new UserAcceptDTO();
        dto.setId(saved.getId());
        dto.setRequestId(saved.getRequest().getId());
        dto.setResponseId(saved.getResponse().getId());
        return dto;
    }
    public boolean isUserAcceptResponse(Integer requestId, Integer responseId) {
        CustomerRequest request = customerRequestRepository.findById(requestId).orElse(null);
        JewelerRespons response = jewelerResponsRepository.findById(responseId).orElse(null);
        if (request == null || response == null) {
            return false;
        }
        return userAcceptRepository.existsByRequest_IdAndResponse_Id(requestId, responseId);
    }
}
