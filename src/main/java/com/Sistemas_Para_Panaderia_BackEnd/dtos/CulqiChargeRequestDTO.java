package com.Sistemas_Para_Panaderia_BackEnd.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CulqiChargeRequestDTO {
    private Long orderId;
    private String culqiToken;
    private String email;
}
