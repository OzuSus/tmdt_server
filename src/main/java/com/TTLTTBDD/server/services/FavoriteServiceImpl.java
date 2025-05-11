package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.exception.UserOrProductNotExistException;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.entity.Favorite;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.repositories.FavoriteRepository;
import com.TTLTTBDD.server.repositories.ProductRepository;
import com.TTLTTBDD.server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Override
    public String addFavorite(Integer userId, Integer productId) {
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);

        if (user == null || product == null) {
            return "User hoặc Product không tồn tại.";
        }
        Favorite favorite = new Favorite();
        favorite.setIdUser(user);
        favorite.setIdProduct(product);
        favoriteRepository.save(favorite);
        return "Thêm thành công vào danh sách yêu thích.";
    }

    @Transactional
    @Override
    public String removeFavoriteByUserIdAndProductId(Integer userId, Integer productId) {
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if (user == null || product == null) {
            return "User hoặc Product không tồn tại.";
        }
        if (!favoriteRepository.existsByIdUserAndIdProduct(user, product)) {
            return "Favorite không tồn tại.";
        }
        favoriteRepository.deleteByIdUserAndIdProduct(user, product);
        return "Xóa thành công product khỏi danh sách yêu thích.";
    }

    @Override
    public List<ProductDTO> getFavoritesDTOByUserId(Integer userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return null;
        }
        return favoriteRepository.findAllByIdUser(user).stream()
                .map(Favorite::getIdProduct)
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    @Override
    public boolean isProductInWishlist(Integer userId, Integer productId) {
        User user = userRepository.findById(userId).orElse(null);
        Product product = productRepository.findById(productId).orElse(null);
        if (user == null || product == null) {
            return false;
        }
        return favoriteRepository.existsByIdUserAndIdProduct(user, product);
    }

    private ProductDTO convertToDTO(Product product) {
        if (product == null) return null;
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrize())
                .quantity(product.getQuantity())
                .description(product.getDescription())
                .image(product.getImage())
                .rating(product.getRating())
                .reviewCount(product.getReview())
                .categoryID(product.getIdCategory().getId())
                .build();
    }
}