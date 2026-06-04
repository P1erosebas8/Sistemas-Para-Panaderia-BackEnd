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
public class SalesAnalyticsDTO {
    private BigDecimal totalSales;
}
