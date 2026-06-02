package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.config.JwtService;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.AuthResponse;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.LoginRequest;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.RegisterRequest;
import com.Sistemas_Para_Panaderia_BackEnd.entities.User;
import com.Sistemas_Para_Panaderia_BackEnd.enums.Role;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final EmailService emailService;

        public String register(RegisterRequest request) {
                if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                        throw new RuntimeException("El email ya está registrado");
                }

                String otp = generateOtp();
                User user = User.builder()
                                .firstName(request.getFirstName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(Role.COMPRADOR)
                                .status("INACTIVO")
                                .otpCode(otp)
                                .otpExpiration(LocalDateTime.now().plusMinutes(10))
                                .build();

                userRepository.save(user);
                emailService.sendOtpEmail(user.getEmail(), otp);

                return "Registro exitoso. Por favor revisa tu correo electrónico para verificar tu cuenta.";
        }

        public AuthResponse login(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                String jwtToken = jwtService.generateToken(user);

                return AuthResponse.builder()
                                .token(jwtToken)
                                .id(user.getId())
                                .firstName(user.getFirstName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .status(user.getStatus())
                                .build();
        }

        public AuthResponse verifyOtp(String email, String otp) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
                        throw new RuntimeException("Código OTP inválido");
                }

                if (user.getOtpExpiration() != null && LocalDateTime.now().isAfter(user.getOtpExpiration())) {
                        throw new RuntimeException("El código OTP ha expirado");
                }

                user.setStatus("ACTIVE");
                user.setOtpCode(null);
                user.setOtpExpiration(null);
                userRepository.save(user);

                String jwtToken = jwtService.generateToken(user);

                return AuthResponse.builder()
                                .token(jwtToken)
                                .id(user.getId())
                                .firstName(user.getFirstName())
                                .email(user.getEmail())
                                .role(user.getRole())
                                .status(user.getStatus())
                                .build();
        }

        public String forgotPassword(String email) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                String otp = generateOtp();
                user.setOtpCode(otp);
                user.setOtpExpiration(LocalDateTime.now().plusMinutes(10));
                userRepository.save(user);

                emailService.sendOtpEmail(user.getEmail(), otp);

                return "Se ha enviado un nuevo código OTP a tu correo.";
        }

        public String resetPassword(String email, String otp, String newPassword) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if (user.getOtpCode() == null || !user.getOtpCode().equals(otp)) {
                        throw new RuntimeException("Código OTP inválido");
                }

                if (user.getOtpExpiration() != null && LocalDateTime.now().isAfter(user.getOtpExpiration())) {
                        throw new RuntimeException("El código OTP ha expirado");
                }

                user.setPassword(passwordEncoder.encode(newPassword));
                user.setOtpCode(null);
                user.setOtpExpiration(null);
                userRepository.save(user);

                return "Contraseña actualizada correctamente.";
        }

        private String generateOtp() {
                return String.format("%06d", new java.util.Random().nextInt(999999));
        }

        public String resendOtp(String email) {
                var user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if ("ACTIVE".equalsIgnoreCase(user.getStatus())) {
                        throw new RuntimeException("La cuenta ya está verificada.");
                }

                String otp = generateOtp();
                user.setOtpCode(otp);
                user.setOtpExpiration(LocalDateTime.now().plusMinutes(10));
                userRepository.save(user);

                emailService.sendOtpEmail(user.getEmail(), otp);

                return "Se ha reenviado un nuevo código OTP a tu correo.";
        }

}
