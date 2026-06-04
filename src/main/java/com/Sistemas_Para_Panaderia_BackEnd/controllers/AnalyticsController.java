package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.SalesAnalyticsDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.TopProductDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Product;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.OrderRepository;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.ProductRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/admin/analytics")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Admin - Analíticas")
public class AnalyticsController {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @GetMapping("/sales")
    public ResponseEntity<SalesAnalyticsDTO> getTotalSales() {
        BigDecimal total = orderRepository.calculateTotalSales();
        if (total == null) {
            total = BigDecimal.ZERO;
        }
        return ResponseEntity.ok(new SalesAnalyticsDTO(total));
    }

    @GetMapping("/top-products")
    public ResponseEntity<List<TopProductDTO>> getTopProducts() {
        List<TopProductDTO> topProducts = orderRepository.findTopProducts(PageRequest.of(0, 5));
        return ResponseEntity.ok(topProducts);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts() {
        List<Product> lowStockProducts = productRepository.findByStockLessThanEqual(10);
        return ResponseEntity.ok(lowStockProducts);
    }
}
