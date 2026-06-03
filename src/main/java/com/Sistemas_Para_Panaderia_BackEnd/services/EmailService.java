package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.entities.Order;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Service
public class EmailService {

    @Value("${google.client.id}")
    private String clientId;

    @Value("${google.client.secret}")
    private String clientSecret;

    @Value("${google.refresh.token}")
    private String refreshToken;

    private final RestTemplate restTemplate = new RestTemplate();

    private String getAccessToken() {
        String tokenUrl = "https://oauth2.googleapis.com/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("refresh_token", refreshToken);
        map.add("grant_type", "refresh_token");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, request, Map.class);
        return (String) response.getBody().get("access_token");
    }

    private void sendEmailViaGmailApi(MimeMessage message) throws Exception {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        message.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);

        String accessToken = getAccessToken();
        String url = "https://gmail.googleapis.com/gmail/v1/users/me/messages/send";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("raw", encodedEmail);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        restTemplate.postForEntity(url, request, String.class);
    }

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            Session session = Session.getInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(toEmail);
            helper.setSubject("Tu código de verificación Briselli");
            helper.setText("Hola,\n\nTu código de verificación es: " + otp
                    + "\n\nEste código expirará pronto. Por favor no lo compartas con nadie.");
            
            sendEmailViaGmailApi(message);
        } catch (Exception e) {
            System.err.println("No se pudo enviar el correo OTP a " + toEmail + ": " + e.getMessage());
        }
    }

    public void sendPaymentReceipt(String to, Order order) {
        try {
            Session session = Session.getInstance(new Properties());
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("¡Gracias por tu compra en Panadería Briselli! - Orden #" + order.getId());

            String htmlContent = "<h1>¡Pago Exitoso!</h1>"
                    + "<p>Hola, hemos recibido tu pago correctamente.</p>"
                    + "<p><b>Detalles de la Orden:</b></p>"
                    + "<ul>"
                    + "<li>Orden ID: " + order.getId() + "</li>"
                    + "<li>Monto Total: S/ " + order.getTotalAmount() + "</li>"
                    + "</ul>"
                    + "<p>Gracias por confiar en Panadería Briselli.</p>";

            helper.setText(htmlContent, true);

            sendEmailViaGmailApi(message);

        } catch (Exception e) {
            System.err.println(
                    "No se pudo enviar el correo de comprobante a la orden " + order.getId() + ": " + e.getMessage());
        }
    }
}
