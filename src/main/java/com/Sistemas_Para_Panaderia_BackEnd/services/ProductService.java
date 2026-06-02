package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.ProductRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.ProductResponseDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Category;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Product;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.CategoryRepository;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .status("DISPONIBLE")
                .category(category)
                .build();

        product = productRepository.save(product);
        return mapToResponseDTO(product);
    }

    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategory(category);

        product = productRepository.save(product);
        return mapToResponseDTO(product);
    }

    public void deactivateProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                
        product.setStatus("INACTIVO");
        productRepository.save(product);
    }

    public ProductResponseDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return mapToResponseDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAvailableProducts() {
        return productRepository.findByStatus("DISPONIBLE").stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryIdAndStatus(categoryId, "DISPONIBLE").stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getLowStockProducts() {
        return productRepository.findByStockLessThanEqual(5).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    private ProductResponseDTO mapToResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus())
                .categoryName(product.getCategory() != null ? product.getCategory().getName() : null)
                .build();
    }
}
