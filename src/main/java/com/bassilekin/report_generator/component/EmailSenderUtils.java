package com.bassilekin.report_generator.component;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmailSenderUtils {

    private final JavaMailSender javaMailSender;

    public void sendMessage(String destinataire, String subject, String body){
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,true);
            helper.setTo(destinataire);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
            e.getMessage();
            throw new RuntimeException("Erreur lors de l'envoi du Mail "+e);
        }
    }

}
