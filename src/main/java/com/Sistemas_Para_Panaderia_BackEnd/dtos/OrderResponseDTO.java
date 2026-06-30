package com.Sistemas_Para_Panaderia_BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private Long userId;
    private LocalDateTime orderDate;
    private String status;
    private BigDecimal totalAmount;
    private List<OrderItemResponseDTO> items;
    private String deliveryAddress;
    private String deliveryPhone;
    private String deliveryNotes;
}
