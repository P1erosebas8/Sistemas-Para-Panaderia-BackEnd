package com.Sistemas_Para_Panaderia_BackEnd.config;

import com.Sistemas_Para_Panaderia_BackEnd.entities.User;
import com.Sistemas_Para_Panaderia_BackEnd.enums.Role;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "admin@briselli.com";

        Optional<User> adminOptional = userRepository.findByEmail(adminEmail);

        if (adminOptional.isEmpty()) {
            User adminUser = User.builder()
                    .firstName("Pierosebas8 Admin")
                    .email(adminEmail)
                    .password(passwordEncoder.encode("BriselliAdmin2026!"))
                    .role(Role.ADMIN)
                    .status("ACTIVE")
                    .build();

            userRepository.save(adminUser);
            log.info("Usuario administrador por defecto creado exitosamente ({}).", adminEmail);
        } else {
            log.info("El usuario administrador ya existe en la base de datos. Omitiendo creación.");
        }
    }
}
