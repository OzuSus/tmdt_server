package com.TTLTTBDD.server.services;

import com.TTLTTBDD.server.models.dto.CategoryDTO;
import com.TTLTTBDD.server.models.dto.UserDTO;
import com.TTLTTBDD.server.models.entity.Category;
import com.TTLTTBDD.server.models.entity.User;
import com.TTLTTBDD.server.repositories.CategotyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategotyRepository categotyRepository;

    public List<CategoryDTO> getAllCategories() {
        return categotyRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CategoryDTO> getCategoryById(int id) {
        return categotyRepository.findCategoryById(id).map(this::convertToDTO);
    }

    public CategoryDTO addCategory(String name, String image) {
        Category category = new Category();
        category.setName(name);
        category.setImage(image);
        categotyRepository.save(category);
        return convertToDTO(category);
    }

    public CategoryDTO updateCategory(int id, String name, String image) {
        Category category = categotyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        category.setName(name);
        if (image != null) {
            category.setImage(image);
        }
        categotyRepository.save(category);
        return convertToDTO(category);
    }
    public CategoryDTO updateCategoryImage(int id, String image) {
        Category category = categotyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (image != null) {
            category.setImage(image);
        }
        categotyRepository.save(category);
        return convertToDTO(category);
    }
    public CategoryDTO updateCategoryName(int id, String name) {
        Category category = categotyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setName(name);
        categotyRepository.save(category);
        return convertToDTO(category);
    }
    private CategoryDTO convertToDTO(Category category) {
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .image(category.getImage())
                .build();
    }
}

