package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.ProductResponseDTO;
import com.Sistemas_Para_Panaderia_BackEnd.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Público - Productos")
public class PublicProductController {

    private final ProductService productService;

    @GetMapping({"", "/"})
    public ResponseEntity<List<ProductResponseDTO>> getAvailableProducts() {
        return ResponseEntity.ok(productService.getAvailableProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }
}
