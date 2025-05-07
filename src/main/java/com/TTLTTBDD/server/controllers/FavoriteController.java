package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.services.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/favorites")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @PostMapping("/add")
    public ResponseEntity<String> addFavorite(@RequestParam Integer userId, @RequestParam Integer productId) {
        //cần 2 param userId và productId, userId là id của người dùng đang login, productId là id của sản phẩm truyền vào
        String result = favoriteService.addFavorite(userId, productId);
        if (result.contains("không tồn tại")) {
            return ResponseEntity.badRequest().body(result);
            //nên alert user
        }
        return ResponseEntity.ok(result);
        //popup thông báo thêm thành công
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> removeFavoriteByUserIdAndProductId(@RequestParam Integer userId, @RequestParam Integer productId) {
        String result = favoriteService.removeFavoriteByUserIdAndProductId(userId, productId);
        if (result.contains("không tồn tại")) {
            return ResponseEntity.badRequest().body(result);
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ProductDTO>> getFavoritesByUserId(@PathVariable Integer userId) {
        List<ProductDTO> favoriteProductDTOs = favoriteService.getFavoritesDTOByUserId(userId);

        if (favoriteProductDTOs == null || favoriteProductDTOs.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }
        return ResponseEntity.ok(favoriteProductDTOs);
    }

    @GetMapping("/inWishlist")
    public ResponseEntity<Boolean> isProductInWishlist(@RequestParam Integer userId, @RequestParam Integer productId) {
        boolean isInWishlist = favoriteService.isProductInWishlist(userId, productId);
        return ResponseEntity.ok(isInWishlist);
    }


}
