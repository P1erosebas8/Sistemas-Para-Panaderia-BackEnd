package com.Sistemas_Para_Panaderia_BackEnd.dtos;

import com.Sistemas_Para_Panaderia_BackEnd.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dni;
    private String email;
    private String phone;
    private String address;
    private Role role;
}
