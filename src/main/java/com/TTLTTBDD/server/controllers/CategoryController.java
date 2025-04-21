package com.TTLTTBDD.server.controllers;


import com.TTLTTBDD.server.models.dto.CategoryDTO;

import com.TTLTTBDD.server.services.CategoryService;
import com.TTLTTBDD.server.utils.loadFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:31415"})
@RequestMapping("api/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    private loadFile fileUploader = new loadFile();

    @GetMapping
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/id")
    public Optional<CategoryDTO> getCategoryById(@RequestParam int id){
        return categoryService.getCategoryById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<CategoryDTO> addCategory(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image) {
        try {
            String fileName = fileUploader.saveFile(image);
            CategoryDTO newCategory = categoryService.addCategory(name, fileName);
            return ResponseEntity.ok(newCategory);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable int id,
            @RequestParam("name") String name,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String fileName = null;
            if (image != null) {
                fileName = fileUploader.saveFile(image);
            }
            CategoryDTO updatedCategory = categoryService.updateCategory(id, name, fileName);
            return ResponseEntity.ok(updatedCategory);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/updateImageCategory/{id}")
    public ResponseEntity<CategoryDTO> updateCategoryImage(
            @PathVariable int id,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            String fileName = null;
            if (image != null) {
                fileName = fileUploader.saveFile(image);
            }
            CategoryDTO updatedCategory = categoryService.updateCategoryImage(id, fileName);
            return ResponseEntity.ok(updatedCategory);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/updateNameCategory/{id}")
    public ResponseEntity<CategoryDTO> updateCategoryName(
            @PathVariable int id,
            @RequestParam("name") String name) {
        CategoryDTO updatedCategory = categoryService.updateCategoryName(id, name);
        return ResponseEntity.ok(updatedCategory);
    }

}
