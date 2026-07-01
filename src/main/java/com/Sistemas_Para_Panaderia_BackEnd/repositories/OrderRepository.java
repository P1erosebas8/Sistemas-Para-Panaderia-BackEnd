package com.Sistemas_Para_Panaderia_BackEnd.repositories;

import com.Sistemas_Para_Panaderia_BackEnd.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    List<Order> findByStatus(String status);

    @org.springframework.data.jpa.repository.Query("SELECT SUM(o.totalAmount) FROM Order o WHERE o.status NOT IN ('PENDIENTE', 'PENDING', 'CANCELADO') AND CAST(o.orderDate AS date) = CURRENT_DATE")
    java.math.BigDecimal calculateTotalSales();

    @org.springframework.data.jpa.repository.Query("SELECT new com.Sistemas_Para_Panaderia_BackEnd.dtos.TopProductDTO(p.name, SUM(oi.quantity)) " +
            "FROM OrderItem oi JOIN oi.product p JOIN oi.order o " +
            "WHERE o.status NOT IN ('PENDIENTE', 'CANCELADO') " +
            "GROUP BY p.id, p.name " +
            "ORDER BY SUM(oi.quantity) DESC")
    List<com.Sistemas_Para_Panaderia_BackEnd.dtos.TopProductDTO> findTopProducts(org.springframework.data.domain.Pageable pageable);
}
