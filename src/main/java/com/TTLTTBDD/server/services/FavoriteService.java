package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.ProductDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface FavoriteService {
    String addFavorite(Integer userId, Integer productId);
    String removeFavoriteByUserIdAndProductId(Integer userId, Integer productId);
    List<ProductDTO> getFavoritesDTOByUserId(Integer userId);
//    ResponseEntity<?> isProductInWishlist(Integer userId, Integer productId);
    boolean isProductInWishlist(Integer userId, Integer productId);
}