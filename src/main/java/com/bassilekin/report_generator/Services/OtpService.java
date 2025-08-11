package com.bassilekin.report_generator.Services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.bassilekin.report_generator.component.EmailSenderUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OtpService {

    @Value("${otp.expiration.time}")
    private long expirationTime;

    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailSenderUtils emailSenderUtils;

    private void saveOtp(String email, String otp) {
        redisTemplate.opsForValue().set(email, otp, expirationTime, TimeUnit.MILLISECONDS);
    }

    private String getOtp(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }

    private void deleteOtp(String email) {
        redisTemplate.delete(email);
    }

    public boolean isOtpValid(String email, String otp) {
        String storedOtp = getOtp(email);
        long expiration = redisTemplate.getExpire(email, TimeUnit.MILLISECONDS);
        if (storedOtp == null || expiration <= 0) {
            return false; //Le code OTP n'existe pas ou à expirer 
        }
        if (storedOtp.equals(otp)) {
            deleteOtp(email);
            return true; // Le code est valide
        }
        return false;
    }

    private String generateOtp(){
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public void sendOtp(String email){
        String subject = "Vérification de votre Email";
        String otpCode = generateOtp();
        String htmlTemplate = null;

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("otp_email.html")) {
            if (is == null) {
                throw new RuntimeException("Le fichier otp_email.html est introuvable dans les ressources");
            }
            htmlTemplate = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (htmlTemplate == null) {
            throw new RuntimeException("Le fichier otp_mail.html est null");
        }
        String emailContent = htmlTemplate.replace("[OTP_CODE]", otpCode);

        emailSenderUtils.sendMessage(email, subject, emailContent);

        saveOtp(email, otpCode);
    }
}
