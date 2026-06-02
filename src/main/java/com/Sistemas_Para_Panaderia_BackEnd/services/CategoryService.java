package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.CategoryDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Category;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDTO createCategory(CategoryDTO request) {
        Category category = Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        
        category = categoryRepository.save(category);
        
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .build();
    }

    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(category -> CategoryDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .description(category.getDescription())
                        .build())
                .collect(Collectors.toList());
    }
}
