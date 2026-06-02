package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.AuthResponse;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.LoginRequest;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.RegisterRequest;
import com.Sistemas_Para_Panaderia_BackEnd.services.AuthService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<AuthResponse> verifyOtp(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.verifyOtp(request.get("email"), request.get("otp")));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.forgotPassword(request.get("email")));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.resetPassword(
                request.get("email"),
                request.get("otp"),
                request.get("newPassword")));
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<String> resendOtp(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok(authService.resendOtp(request.get("email")));
    }
}
