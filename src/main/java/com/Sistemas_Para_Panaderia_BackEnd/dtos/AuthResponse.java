package com.Sistemas_Para_Panaderia_BackEnd.dtos;

import com.Sistemas_Para_Panaderia_BackEnd.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String token;
    private Long id;
    private String firstName;
    private String email;
    private Role role;
    private String status;
}
