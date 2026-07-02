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
            String htmlContent = "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 30px; border: 1px solid #e5ded6; border-radius: 16px; background-color: #ffffff;\">"
                    + "<div style=\"text-align: center; margin-bottom: 24px;\">"
                    + "<h2 style=\"color: #6f4014; margin: 0; font-size: 24px;\">Verificación de Seguridad</h2>"
                    + "</div>"
                    + "<p style=\"color: #51443b; font-size: 16px; line-height: 1.6;\">Hola,</p>"
                    + "<p style=\"color: #51443b; font-size: 16px; line-height: 1.6;\">Gracias por elegir <b>Panadería Briselli</b>. Tu código de acceso seguro es:</p>"
                    + "<div style=\"background-color: #fbf9f5; padding: 24px; text-align: center; border-radius: 12px; margin: 32px 0; border: 1px dashed #d1c1a7;\">"
                    + "<span style=\"font-size: 40px; font-weight: 900; letter-spacing: 8px; color: #6f4014;\">" + otp + "</span>"
                    + "</div>"
                    + "<p style=\"color: #888; font-size: 13px; text-align: center; margin-top: 32px;\">Este código es temporal. Si no solicitaste este acceso, puedes ignorar este correo de forma segura.</p>"
                    + "</div>";
            
            helper.setText(htmlContent, true);
            
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

            String htmlContent = "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 30px; border: 1px solid #e5ded6; border-radius: 16px; background-color: #ffffff;\">"
                    + "<div style=\"text-align: center; margin-bottom: 24px;\">"
                    + "<h2 style=\"color: #165128; margin: 0; font-size: 26px;\">¡Compra Exitosa! 🎉</h2>"
                    + "</div>"
                    + "<p style=\"color: #51443b; font-size: 16px; line-height: 1.6;\">Hola, hemos recibido tu pago correctamente. Tu pedido ya está siendo preparado con mucho amor en nuestros hornos artesanales.</p>"
                    + "<div style=\"background-color: #fbf9f5; padding: 24px; border-radius: 12px; margin: 32px 0; border: 1px solid #e5ded6;\">"
                    + "<h3 style=\"color: #6f4014; margin-top: 0; margin-bottom: 16px; font-size: 18px;\">Detalle de tu Orden</h3>"
                    + "<p style=\"margin: 8px 0; color: #51443b; font-size: 15px;\"><b style=\"color: #1b1c1a;\">Código de Pedido:</b> #" + String.format("%06d", order.getId()) + "</p>"
                    + "<p style=\"margin: 8px 0; font-size: 20px;\"><b style=\"color: #1b1c1a;\">Total Pagado:</b> <span style=\"color: #165128; font-weight: 900;\">S/ " + String.format("%.2f", order.getTotalAmount()) + "</span></p>"
                    + "</div>"
                    + "<p style=\"color: #51443b; font-size: 15px; line-height: 1.6;\">Recuerda que puedes hacer el seguimiento del estado de tu entrega directamente desde la sección <b>'Mis Compras'</b> en la plataforma.</p>"
                    + "<p style=\"color: #888; font-size: 13px; text-align: center; margin-top: 40px; padding-top: 20px; border-top: 1px solid #eee;\">Panadería Briselli - Calidad y Tradición</p>"
                    + "</div>";

            helper.setText(htmlContent, true);

            sendEmailViaGmailApi(message);

        } catch (Exception e) {
            System.err.println(
                    "No se pudo enviar el correo de comprobante a la orden " + order.getId() + ": " + e.getMessage());
        }
    }
}
