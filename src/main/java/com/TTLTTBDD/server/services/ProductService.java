package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.dto.UserDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.repositories.ProductRepository;
import com.TTLTTBDD.server.repositories.UserRepository;
import com.TTLTTBDD.server.utils.loadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ProductDTO> getProductById(int id) {
        return productRepository.findProductById(id).map(this::convertToDTO);
    }

    public List<ProductDTO> getProductsByCategoryID(int idCategory) {
        return productRepository.findByIdCategory_Id(idCategory).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductDTO> getProductsByTag(String tag) {
        return productRepository.findByTagIgnoreCase(tag).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public String deleteProduct(int idProduct) {
        Optional<Product> existingProduct = productRepository.findProductById(idProduct);
        if (existingProduct.isEmpty()) {
            return "Product does not exist";
        }
        productRepository.deleteById(idProduct);
        return "Xóa thành công";
    }


    public ProductDTO create(Product product) {
        Product product1 = productRepository.save(product);
        return convertToDTO(product1);
    }

    private ProductDTO convertToDTO(Product product) {
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
                .tag(product.getTag())
                .build();
    }


    public ProductDTO addProduct(String name, BigDecimal quantity, Double prize, String description, Category idCategory, String fileName) {
        Product product = new Product();
        product.setName(name);
        product.setImage(fileName);
        product.setQuantity(quantity);
        product.setPrize(prize);
        product.setDescription(description);
        product.setIdCategory(idCategory);
        product.setRating(0.0);
        product.setReview(0);
        productRepository.save(product);
        return convertToDTO(product);
    }

    public ProductDTO editProduct(int id, String name, BigDecimal quantity, Double prize, String description, Category idCategory, String fileName) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        if (name != null) {
            product.setName(name);
        }
        if (quantity != null) {
            product.setQuantity(quantity);
        }
        if (prize != null) {
            product.setPrize(prize);
        }
        if (description != null) {
            product.setDescription(description);
        }
        if (idCategory != null) {
            product.setIdCategory(idCategory);
        }
        if (fileName != null) {
            product.setImage(fileName);
        }
        productRepository.save(product);
        return convertToDTO(product);
    }
}
