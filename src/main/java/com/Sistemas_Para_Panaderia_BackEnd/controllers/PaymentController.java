package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.CulqiChargeRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.services.PaymentService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Pagos")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/charge")
    public ResponseEntity<String> createCharge(@RequestBody CulqiChargeRequestDTO request) {
        String resultMessage = paymentService.processCulqiCharge(request);
        return ResponseEntity.ok(resultMessage);
    }
}
