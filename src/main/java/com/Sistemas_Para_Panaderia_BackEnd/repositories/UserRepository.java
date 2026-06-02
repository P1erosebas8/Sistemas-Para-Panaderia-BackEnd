package com.Sistemas_Para_Panaderia_BackEnd.repositories;

import com.Sistemas_Para_Panaderia_BackEnd.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método clave utilizado por Spring Security y AuthService para buscar si el
    // usuario existe
    Optional<User> findByEmail(String email);
}
