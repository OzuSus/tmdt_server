package com.TTLTTBDD.server.controllers;

import com.TTLTTBDD.server.models.dto.CategoryDTO;
import com.TTLTTBDD.server.models.dto.ProductDTO;
import com.TTLTTBDD.server.models.dto.UserDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.Product;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.services.ProductService;
import com.TTLTTBDD.server.utils.loadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;
    private loadFile fileUploader = new loadFile();

    @GetMapping
    public List<ProductDTO> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/id")
    public Optional<ProductDTO> getProductById(@RequestParam int id) {
        return productService.getProductById(id);
    }

    @GetMapping("/idCategory")
    public List<ProductDTO> getProductsByCategoryID(@RequestParam int idCategory) {
        return productService.getProductsByCategoryID(idCategory);
    }

    @GetMapping("/tag")
    public List<ProductDTO> getProductsByTag(@RequestParam String tag) {
        return productService.getProductsByTag(tag);
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ProductDTO> createProduct(
            @RequestParam("name") String name,
            @RequestParam("quantity") BigDecimal quantity,
            @RequestParam("prize") Double prize,
            @RequestParam("description") String description,
            @RequestParam("id_category") Category id_category,
            @RequestParam("image") MultipartFile image) {
        try {
            String fileName = fileUploader.saveFile(image);
            ProductDTO newProduct = productService.addProduct(name, quantity, prize, description, id_category, fileName);
            return ResponseEntity.ok(newProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/editProduct/{id}")
    public ResponseEntity<ProductDTO> editProduct(
            @PathVariable int id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "quantity", required = false) BigDecimal quantity,
            @RequestParam(value = "prize", required = false) Double prize,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "id_category", required = false) Category id_category,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String fileName = null;
            if (image != null) {
                fileName = fileUploader.saveFile(image);
            }
            ProductDTO editProduct = productService.editProduct(id, name, quantity, prize, description, id_category, fileName);
            return ResponseEntity.ok(editProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/deleteProduct")
    public String deleteProduct(@RequestParam int idProduct) {
        return productService.deleteProduct(idProduct);
    }


}
