package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.ChangePasswordRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.UpdateProfileRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.UserProfileDTO;
import com.Sistemas_Para_Panaderia_BackEnd.services.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Usuarios")

public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDTO> getMyProfile(Principal principal) {
        return ResponseEntity.ok(userService.getMyProfile(principal.getName()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileDTO> updateMyProfile(Principal principal,
            @RequestBody UpdateProfileRequestDTO request) {
        return ResponseEntity.ok(userService.updateMyProfile(principal.getName(), request));
    }

    @PatchMapping("/me/password")
    public ResponseEntity<String> changePassword(Principal principal, @RequestBody ChangePasswordRequestDTO request) {
        return ResponseEntity.ok(userService.changePassword(principal.getName(), request));
    }
}
