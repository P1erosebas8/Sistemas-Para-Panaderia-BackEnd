package com.Sistemas_Para_Panaderia_BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private String imageUrl;
    private Long categoryId;
}
