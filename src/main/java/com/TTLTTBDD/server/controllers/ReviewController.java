package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.ReviewRequestDTO;
import com.TTLTTBDD.server.models.dto.ReviewResponseDTO;
import com.TTLTTBDD.server.models.entity.Review;
import com.TTLTTBDD.server.services.ProductService;
import com.TTLTTBDD.server.services.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService) {
        this.reviewService = reviewService;
        this.productService = productService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/check-purchased")
    public ResponseEntity<Boolean> checkUserPurchased(@RequestParam Integer userId, @RequestParam Integer productId) {
        boolean hasPurchased = reviewService.hasUserPurchasedProduct(userId, productId);
        return ResponseEntity.ok(hasPurchased);
    }

    @GetMapping("/check-reviewed")
    public ResponseEntity<Boolean> checkUserReviewed(@RequestParam Integer userId, @RequestParam Integer productId) {
        boolean hasReviewed = reviewService.hasUserReviewedProduct(userId, productId);
        return ResponseEntity.ok(hasReviewed);
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeReview(@RequestBody ReviewRequestDTO dto) {
        // Kiểm tra đã mua chưa
        if (!reviewService.hasUserPurchasedProduct(dto.getUserId(), dto.getProductId())) {
            return ResponseEntity.badRequest().body("User chưa mua sản phẩm này.");
        }

        // Kiểm tra đã review chưa
        if (reviewService.hasUserReviewedProduct(dto.getUserId(), dto.getProductId())) {
            return ResponseEntity.badRequest().body("User đã review sản phẩm này rồi.");
        }

        Review review = reviewService.writeReview(
                dto.getUserId(),
                dto.getProductId(),
                dto.getComment(),
                dto.getRating()
        );

        // Cập nhật lại rating sản phẩm
        productService.updateProductRating(dto.getProductId());

        // Có thể trả về đơn giản như sau:
        return ResponseEntity.ok("Review thành công!");
    }
}