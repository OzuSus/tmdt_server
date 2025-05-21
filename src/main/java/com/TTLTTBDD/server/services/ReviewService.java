package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.Review;
import com.TTLTTBDD.server.repositories.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final OderDetailRepository orderDetailRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository, OderDetailRepository orderDetailRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.orderDetailRepository = orderDetailRepository;
    }

    public List<Review> getReviewsByProductId(Integer productId) {
        return reviewRepository.findByProductId(productId);
    }

    public boolean hasUserPurchasedProduct(Integer userId, Integer productId) {
        return orderDetailRepository.hasUserPurchasedProduct(userId, productId);
    }

    @Transactional
    public Review writeReview(Integer userId, Integer productId, String comment, Integer rating) {
        if (!hasUserPurchasedProduct(userId, productId)) {
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