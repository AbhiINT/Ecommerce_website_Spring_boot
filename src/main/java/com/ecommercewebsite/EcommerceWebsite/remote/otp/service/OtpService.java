package com.ecommercewebsite.EcommerceWebsite.remote.otp.service;



import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ecommercewebsite.EcommerceWebsite.remote.otp.entity.OtpEntity;
import com.ecommercewebsite.EcommerceWebsite.repository.OtpRepository;



import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService  {


    private final  OtpRepository otpRepository;
 


    private final JavaMailSender javaMailSender;

   
    public boolean saveOtp(String email, String otp) {
        try {
            OtpEntity otpEntity = new OtpEntity();
            otpEntity.setEmail(email);
            otpEntity.setOtp(otp);
            otpEntity.setCreatedTime(new Timestamp(System.currentTimeMillis()));
            otpEntity.setExpirationTime(new Timestamp(System.currentTimeMillis() + 2 * 60 * 1000)); // 2 minutes expiration
            otpRepository.save(otpEntity);
            return true;
        } catch (Exception e) {
            if (e instanceof DataIntegrityViolationException) {
                handleDuplicateEmail(email, otp);
            } else {
                // Log other unexpected exceptions for debugging
                e.printStackTrace();
            }
            return false;
        }
    }
    

    private void handleDuplicateEmail(String email, String otp) {
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmail(email);
        optionalOtpEntity.ifPresentOrElse(
                entity -> {
                    entity.setOtp(otp);
                    entity.setExpirationTime(new Timestamp(System.currentTimeMillis() + 5 * 60 * 1000)); // Update expiration time
                    otpRepository.save(entity);
                },
                () -> {

                });
    }

    public String verifyOtp(String email, String enteredOtp) {
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmailAndExpirationTimeAfter(email, new Timestamp(System.currentTimeMillis()));
        return optionalOtpEntity.map(otpEntity -> {
            if (otpEntity.getOtp().equals(enteredOtp)) {
                return "valid"; 
            } else {
                return "invalid";
            }
        }).orElse("expired"); 
    }

    public boolean resendOtp(String email) {
        Optional<OtpEntity> optionalOtpEntity = otpRepository.findByEmailAndExpirationTimeAfter(email, new Timestamp(System.currentTimeMillis()));
    
        optionalOtpEntity.ifPresent(entity -> otpRepository.delete(entity));
    
        String newOtp = generateOtp();
        saveOtp(email, newOtp);
    
        return sendOtpEmail(email, newOtp);
    }
    

    public String generateOtp() {
        Random random = new Random();
        return String.format("%04d", random.nextInt(10000));
    }

    
 
    public boolean sendOtpEmail(String email, String otp) {
       try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("OTP Verification");
            message.setText("Your OTP for registration is: " + otp);
            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
