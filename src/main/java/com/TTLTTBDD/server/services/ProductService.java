package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.Review;
import com.TTLTTBDD.server.repositories.ProductRepository;
import com.TTLTTBDD.server.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
    }

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

    public List<ProductDTO> getProductByRating(double rating) {
        return productRepository.findByRating(rating).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void updateProductRating(Integer productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        double average = reviews.stream().mapToInt(Review::getRatingStar).average().orElse(4);
        Product product = productRepository.findById(productId).orElseThrow();
        product.setRating(average);
        productRepository.save(product);
    }

    public List<ProductDTO> getProductBetween(double min, double max) {
        return productRepository.findByPrizeBetween(min, max).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    //    public List<ProductDTO> getFilteredProducts(
//            Integer idCategory,
//            Double rating,
//            Double minPrice,
//            Double maxPrice,
//            String sortDirection // "asc" or "desc"
//    ) {
//       List<ProductDTO> products = productRepository.findAll().stream()
//               .map(this::convertToDTO)
//               .collect(Collectors.toList());
//        if (idCategory != null) {
//            products.removeIf(productDTO -> !productDTO.getCategoryID().equals(idCategory));
//        }
//        if (rating != null) {
//            products.removeIf(productDTO -> !productDTO.getRating().equals(rating));
//        }
//        if(minPrice != null || maxPrice != null){
//            products.removeIf(productDTO -> productDTO.getPrice() < minPrice || productDTO.getPrice() > maxPrice);
//        }
//        products.sort((a, b) -> {
//            if (a.getPrice() == null || b.getPrice() == null) return 0;
//            return "desc".equalsIgnoreCase(sortDirection)
//                    ? Double.compare(b.getPrice(), a.getPrice())
//                    : Double.compare(a.getPrice(), b.getPrice());
//        });
//        return products;
//    }
    public List<ProductDTO> getFilteredProducts(
            Integer idCategory,
            Double rating,
            Double minPrice,
            Double maxPrice,
            String sortDirection
    ) {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .filter(product -> idCategory == null || product.getCategoryID().equals(idCategory))
                .filter(product -> rating == null || product.getRating().equals(rating))
                .filter(product -> {
                    Double price = product.getPrice();
                    if (price == null) return false;
                    if (minPrice != null && price < minPrice) return false;
                    return maxPrice == null || price <= maxPrice;
                })
                .sorted((a, b) -> {
                    Double priceA = a.getPrice();
                    Double priceB = b.getPrice();
                    if (priceA == null || priceB == null) return 0;
                    return "desc".equalsIgnoreCase(sortDirection)
                            ? Double.compare(priceB, priceA)
                            : Double.compare(priceA, priceB);
                })
                .collect(Collectors.toList());
    }


}
