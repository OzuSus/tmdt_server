package com.TTLTTBDD.server.services;


import com.TTLTTBDD.server.models.dto.CartDetailDTO;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.entity.Cart;
import com.TTLTTBDD.server.models.entity.CartDetail;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.repositories.CartDetailRepository;
import com.TTLTTBDD.server.repositories.CartRepository;
import com.TTLTTBDD.server.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartDetailService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartDetailRepository cartDetailRepository;

    public CartDetailDTO addProductToCartDetail(Integer idUser, Integer idProduct) {
        Cart cart = cartRepository.findByIdUser_Id(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Cart không tồn tại cho User này."));

        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại."));

        if (product.getQuantity().compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Sản phẩm không còn đủ hàng trong kho.");
        }

        CartDetail cartDetail = cartDetailRepository.findByIdCart_IdAndIdProduct_Id(cart.getId(), idProduct)
                .orElseGet(() -> {
                    CartDetail newCartDetail = new CartDetail();
                    newCartDetail.setIdCart(cart);
                    newCartDetail.setIdProduct(product);
                    newCartDetail.setQuantity(1);
                    return newCartDetail;
                });

        if (cartDetail.getId() != null) {
            cartDetail.setQuantity(cartDetail.getQuantity() + 1);
        }
        product.setQuantity(product.getQuantity().subtract(BigDecimal.ONE));
        productRepository.save(product);

        CartDetail savedCartDetail = cartDetailRepository.save(cartDetail);

        return new CartDetailDTO(
                savedCartDetail.getId(),
                savedCartDetail.getIdCart().getId(),
                savedCartDetail.getIdProduct().getId(),
                savedCartDetail.getQuantity()
        );
    }

    public void removeProductFromCartDetail(Integer idUser, Integer idProduct) {
        Cart cart = cartRepository.findByIdUser_Id(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Cart không tồn tại cho User này."));

        CartDetail cartDetail = cartDetailRepository.findByIdCart_IdAndIdProduct_Id(cart.getId(), idProduct)
                .orElseThrow(() -> new IllegalArgumentException("CartDetail không tồn tại."));

        cartDetailRepository.delete(cartDetail);
    }

    public List<ProductDTO> getProductsInCartByUserId(Integer idUser) {
        Cart cart = cartRepository.findByIdUser_Id(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Cart không tồn tại cho User này."));

        List<CartDetail> cartDetails = cartDetailRepository.findAllByIdCart_Id(cart.getId());

        return cartDetails.stream()
                .map(cartDetail -> {
                    Product product = cartDetail.getIdProduct();
                    return ProductDTO.builder()
                            .id(product.getId())
                            .name(product.getName())
                            .price(product.getPrize())
                            .quantity(BigDecimal.valueOf(cartDetail.getQuantity())) // Lấy số lượng từ cartDetail
                            .image(product.getImage())
                            .description(product.getDescription())
                            .reviewCount(product.getReview())
                            .rating(product.getRating())
                            .categoryID(product.getIdCategory().getId())
                            .build();
                })
                .collect(Collectors.toList());
    }
    public CartDetailDTO increaseProductQuantity(Integer idUser, Integer idProduct) {
        Cart cart = cartRepository.findByIdUser_Id(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Cart không tồn tại cho User này."));

        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại."));

        if (product.getQuantity().compareTo(BigDecimal.ONE) < 0) {
            throw new IllegalArgumentException("Sản phẩm không còn đủ hàng trong kho.");
        }

        CartDetail cartDetail = cartDetailRepository.findByIdCart_IdAndIdProduct_Id(cart.getId(), idProduct)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ hàng."));

        cartDetail.setQuantity(cartDetail.getQuantity() + 1);
        product.setQuantity(product.getQuantity().subtract(BigDecimal.ONE));

        productRepository.save(product);
        CartDetail updatedCartDetail = cartDetailRepository.save(cartDetail);

        return new CartDetailDTO(
                updatedCartDetail.getId(),
                updatedCartDetail.getIdCart().getId(),
                updatedCartDetail.getIdProduct().getId(),
                updatedCartDetail.getQuantity()
        );
    }

    public void decreaseProductQuantity(Integer idUser, Integer idProduct) {
        Cart cart = cartRepository.findByIdUser_Id(idUser)
                .orElseThrow(() -> new IllegalArgumentException("Cart không tồn tại cho User này."));

        CartDetail cartDetail = cartDetailRepository.findByIdCart_IdAndIdProduct_Id(cart.getId(), idProduct)
                .orElseThrow(() -> new IllegalArgumentException("Sản phẩm không tồn tại trong giỏ hàng."));

        if (cartDetail.getQuantity() == 1) {

        } else {
            cartDetail.setQuantity(cartDetail.getQuantity() - 1);
            cartDetailRepository.save(cartDetail);
        }

        Product product = cartDetail.getIdProduct();
        product.setQuantity(product.getQuantity().add(BigDecimal.ONE));
        productRepository.save(product);
    }
}

