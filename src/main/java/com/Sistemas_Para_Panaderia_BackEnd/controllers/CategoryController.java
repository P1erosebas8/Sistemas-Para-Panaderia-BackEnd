package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.CategoryDTO;
import com.Sistemas_Para_Panaderia_BackEnd.services.CategoryService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Categorías")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping({ "", "/" })
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @PostMapping({ "", "/" })
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.createCategory(request));
    }
}
