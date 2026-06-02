package com.Sistemas_Para_Panaderia_BackEnd.repositories;

import com.Sistemas_Para_Panaderia_BackEnd.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByStatus(String status);
    List<Product> findByCategoryIdAndStatus(Long categoryId, String status);
    List<Product> findByStockLessThanEqual(Integer threshold);
}
