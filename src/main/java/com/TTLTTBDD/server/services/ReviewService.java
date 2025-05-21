package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.ReviewResponseDTO;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.Review;
import com.TTLTTBDD.server.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    private final OrderService orderService;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(OrderService orderService, ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository, OrderDetailRepository orderDetailRepository) {
        this.orderService = orderService;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public List<ReviewResponseDTO> getReviewsByProductId(Integer productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        List<ReviewResponseDTO> result = new ArrayList<>();

        for (Review review : reviews) {
            ReviewResponseDTO responseDTO = new ReviewResponseDTO();
            responseDTO.setId(review.getId());
            responseDTO.setComment(review.getComment());
            responseDTO.setRating(review.getRatingStar());
            responseDTO.setUserName(review.getUser().getUsername());
            responseDTO.setCreatedAt(review.getDate());
            result.add(responseDTO);
        }

        return result;
    }

    public boolean hasUserReviewedProduct(Integer userId, Integer productId) {
        return reviewRepository.existsByUserIdAndProductId(userId, productId);
    }

    @Transactional
    public Review writeReview(Integer userId, Integer productId, String comment, Integer rating) {
        if (!orderService.hasUserPurchasedProduct(userId, productId)) {
            throw new IllegalStateException("User has not purchased this product");
        }

        Review review = new Review();
        review.setUser(userRepository.findById(userId).orElseThrow());
        review.setProduct(productRepository.findById(productId).orElseThrow());
        review.setComment(comment);
        review.setRatingStar(rating);
        review.setDate(LocalDate.now());
        Review saved = reviewRepository.save(review);

        updateProductRating(productId);
        return saved;
    }

    public void updateProductRating(Integer productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        double avg = reviews.stream().mapToInt(Review::getRatingStar).average().orElse(0.0);

        Product product = productRepository.findById(productId).orElseThrow();
        product.setRating(avg);
        product.setReview(reviews.size());
        productRepository.save(product);
    }
}