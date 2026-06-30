package com.Sistemas_Para_Panaderia_BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequestDTO {
    private Long userId;
    private List<OrderItemRequestDTO> items;
    
    private String deliveryAddress;
    private String deliveryPhone;
    private String deliveryNotes;
}
