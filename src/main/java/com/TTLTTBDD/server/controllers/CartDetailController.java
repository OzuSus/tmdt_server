package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.CartDetailDTO;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.entity.CartDetail;
import com.TTLTTBDD.server.services.CartDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-details")
@CrossOrigin(origins = "http://localhost:3000")
public class CartDetailController {
    @Autowired
    private CartDetailService cartDetailService;

    @PostMapping("/add")
    public ResponseEntity<CartDetailDTO> addProductToCartDetail(
            @RequestParam Integer idProduct,
            @RequestParam Integer idUser) {
        CartDetailDTO cartDetailDTO = cartDetailService.addProductToCartDetail(idUser, idProduct);

        return ResponseEntity.ok(cartDetailDTO);
    }
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeProductFromCartDetail(
            @RequestParam Integer idUser,
            @RequestParam Integer idProduct) {
        cartDetailService.removeProductFromCartDetail(idUser, idProduct);

        return ResponseEntity.ok("Sản phẩm đã được xóa khỏi giỏ hàng.");
    }
    @GetMapping("/user-products")
    public ResponseEntity<List<ProductDTO>> getProductsInCart(
            @RequestParam Integer idUser) {
        List<ProductDTO> products = cartDetailService.getProductsInCartByUserId(idUser);
        return ResponseEntity.ok(products);
    }
    @CrossOrigin(origins = "http://localhost:31415")
    @PostMapping("/increase-quantity")
    public ResponseEntity<CartDetailDTO> increaseProductQuantity(
            @RequestParam Integer idUser,
            @RequestParam Integer idProduct) {
        CartDetailDTO updatedCartDetail = cartDetailService.increaseProductQuantity(idUser, idProduct);
        return ResponseEntity.ok(updatedCartDetail);
    }
    @CrossOrigin(origins = "http://localhost:31415")
    @PostMapping("/decrease-quantity")
    public ResponseEntity<String> decreaseProductQuantity(
            @RequestParam Integer idUser,
            @RequestParam Integer idProduct) {
        cartDetailService.decreaseProductQuantity(idUser, idProduct);
        return ResponseEntity.ok("Số lượng sản phẩm đã được cập nhật.");
    }
}
