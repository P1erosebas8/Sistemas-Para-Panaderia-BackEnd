package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.dtos.CulqiChargeRequestDTO;
import com.Sistemas_Para_Panaderia_BackEnd.entities.Order;
import com.Sistemas_Para_Panaderia_BackEnd.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;
    private final EmailService emailService;

    @Value("${culqi.secret-key}")
    private String secretKey;

    public String processCulqiCharge(CulqiChargeRequestDTO request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Orden no encontrada"));

        if (!"PENDIENTE".equals(order.getStatus())) {
            throw new RuntimeException("La orden ya fue pagada o cancelada");
        }

        // El monto en Culqi debe ser en céntimos (ej. S/ 10.50 -> 1050)
        int amount = order.getTotalAmount().multiply(new BigDecimal(100)).intValue();

        // 1. Preparar Headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 2. Preparar el Body
        Map<String, Object> body = new HashMap<>();
        body.put("amount", amount);
        body.put("currency_code", "PEN");
        body.put("email", request.getEmail());
        body.put("source_id", request.getCulqiToken());

        // 3. Crear el HttpEntity con el Body y los Headers
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        try {
            // 4. Hacer la petición POST al API de Culqi
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.culqi.com/v2/charges",
                    entity,
                    String.class
            );

            // Si pasa de la línea anterior, significa que devolvió un código HTTP 2xx (Éxito)
            order.setStatus("PAGADO");
            orderRepository.save(order);

            // 5. Enviar Comprobante de Pago por Correo
            try {
                emailService.sendPaymentReceipt(request.getEmail(), order);
            } catch (Exception ex) {
                // MUY IMPORTANTE: Solo registramos el error, NO lanzamos la excepción
                // Porque el pago ya fue cobrado exitosamente de la tarjeta del cliente
                log.error("El pago (Orden #{}) fue exitoso, pero ocurrió un error al enviar el correo a {}: {}", 
                        order.getId(), request.getEmail(), ex.getMessage());
            }

            return "Pago procesado exitosamente.";

        } catch (HttpClientErrorException e) {
            // Captura errores 4xx del API de Culqi (fondos insuficientes, tarjeta rechazada, etc.)
            throw new RuntimeException("Error en Culqi: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            // Errores de red u otros imprevistos (Solo afecta si Culqi falló al cobrar)
            throw new RuntimeException("Error de conexión al procesar el pago: " + e.getMessage(), e);
        }
    }
}
