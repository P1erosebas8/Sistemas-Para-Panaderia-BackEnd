package com.Sistemas_Para_Panaderia_BackEnd.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/demo")
@Tag(name = "Demo")

public class DemoController {

    @GetMapping("/saludo")
    public ResponseEntity<String> saludoProtegido() {
        return ResponseEntity.ok("¡Hola! Si ves esto, tu token JWT es válido y tienes acceso a la panadería.");
    }
}