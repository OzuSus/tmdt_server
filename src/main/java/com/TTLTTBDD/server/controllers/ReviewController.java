package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.ReviewRequestDTO;
import com.TTLTTBDD.server.models.dto.ReviewResponseDTO;
import com.TTLTTBDD.server.models.entity.Review;
import com.TTLTTBDD.server.services.OrderService;
import com.TTLTTBDD.server.services.ProductService;
import com.TTLTTBDD.server.services.ReviewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;
    private final OrderService orderService;

    @Autowired
    public ReviewController(ReviewService reviewService, ProductService productService, OrderService orderService) {
        this.reviewService = reviewService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDTO>> getReviewsByProduct(@PathVariable Integer productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(productId));
    }

    @GetMapping("/check-reviewed")
    public ResponseEntity<Boolean> checkUserReviewed(@RequestParam Integer userId, @RequestParam Integer productId) {
        boolean hasReviewed = reviewService.hasUserReviewedProduct(userId, productId);
        return ResponseEntity.ok(hasReviewed);
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeReview(@RequestBody ReviewRequestDTO dto) {
        // Kiểm tra rating phải nằm trong khoảng 1 đến 5
        if (dto.getRating() < 1 || dto.getRating() > 5) {
            return ResponseEntity.badRequest().body("Số sao đánh giá phải từ 1 đến 5.");
        }
        
        // Kiểm tra đã mua chưa
        if (!orderService.hasUserPurchasedProduct(dto.getUserId(), dto.getProductId())) {
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