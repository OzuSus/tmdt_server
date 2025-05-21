package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.ReviewRequestDTO;
import com.TTLTTBDD.server.models.entity.Review;
import com.TTLTTBDD.server.services.ProductService;
import com.TTLTTBDD.server.services.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Review>> getReviewsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> checkUserPurchased(
            @RequestParam Integer userId,
            @RequestParam Integer productId
    ) {
        boolean hasPurchased = reviewService.hasUserPurchasedProduct(userId, productId);
        return ResponseEntity.ok(hasPurchased);
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeReview(@RequestBody ReviewRequestDTO reviewRequestDTO) {
        // Kiểm tra user đã mua sản phẩm chưa
        boolean hasPurchased = reviewService.hasUserPurchasedProduct(
                reviewRequestDTO.getUserId(),
                reviewRequestDTO.getProductId()
        );

        if (!hasPurchased) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("User has not purchased this product");
        }

        Review review = reviewService.writeReview(
                reviewRequestDTO.getUserId(),
                reviewRequestDTO.getProductId(),
                reviewRequestDTO.getComment(),
                reviewRequestDTO.getRating()
        );

        // Cập nhật lại rating sản phẩm
        productService.updateProductRating(reviewRequestDTO.getProductId());

        return ResponseEntity.ok(review);
    }
}