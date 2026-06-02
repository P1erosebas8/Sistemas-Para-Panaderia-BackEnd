package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.OrderRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.dtos.OrderResponseDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.User;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.UserRepository;
import com.Sistemas_Para_Panaderia_BackEnd.services.OrderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Público - Órdenes")
public class PublicOrderController {

    private final OrderService orderService;
    private final UserRepository userRepository;

    @PostMapping({"", "/"})
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request, Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        request.setUserId(user.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(request));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getMyOrders(Principal principal) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        return ResponseEntity.ok(orderService.getOrdersByUser(user.getId()));
    }
}
