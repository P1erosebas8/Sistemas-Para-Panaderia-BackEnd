package com.Sistemas_Para_Panaderia_BackEnd.services;

import com.Sistemas_Para_Panaderia_BackEnd.entities.Order;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Tu código de verificación Briselli");
        message.setText("Hola,\n\nTu código de verificación es: " + otp
                + "\n\nEste código expirará pronto. Por favor no lo compartas con nadie.");
        mailSender.send(message);
    }

    public void sendPaymentReceipt(String to, Order order) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
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

            helper.setText(htmlContent, true); // true indica que el texto es HTML

            mailSender.send(message);

        } catch (MessagingException e) {
            System.err.println(
                    "No se pudo enviar el correo de comprobante a la orden " + order.getId() + ": " + e.getMessage());
        }
    }
}
