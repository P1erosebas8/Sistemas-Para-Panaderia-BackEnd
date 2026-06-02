package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.ProductRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.ProductResponseDTO;
import com.Sistemas_Para_Panaderia_BackEnd.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin - Productos")
public class AdminProductController {

    private final ProductService productService;

    @GetMapping("/all")
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<ProductResponseDTO>> getLowStockProducts() {
        return ResponseEntity.ok(productService.getLowStockProducts());
    }

    @PostMapping({"", "/"})
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.ok("Producto desactivado exitosamente.");
    }
}
