package com.ecommercewebsite.EcommerceWebsite.auth.adminAuth.adminAuthController1;

import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.ecommercewebsite.EcommerceWebsite.DTO.OtpVerificationRequest;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.auth.adminAuth.adminAuthService.AdminAuthService;
import com.ecommercewebsite.EcommerceWebsite.auth.dto.LoginRequest;
import com.ecommercewebsite.EcommerceWebsite.auth.dto.RegistrationRequest;
import com.ecommercewebsite.EcommerceWebsite.auth.dto.RegistrationResponse;
import com.ecommercewebsite.EcommerceWebsite.model.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.model.admin.repository.AdminRepository;
import com.ecommercewebsite.EcommerceWebsite.remote.otp.service.OtpService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/auth/admin")
@RequiredArgsConstructor
public class AdminAuthController {

    private final AdminRepository adminRepository;
    
    private final OtpService otpService;
    private final AdminAuthService authService;
    


     @PostMapping("/signup")
    public ResponseEntity<RegistrationResponse> signUp(@RequestBody RegistrationRequest registrationRequest){
      
        return ResponseEntity.ok(authService.handleSignUpAdmin(registrationRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<ReqRes> signIn(@RequestBody LoginRequest signInRequest){
        System.out.println(signInRequest.toString());
        return ResponseEntity.ok(authService.signInAdmin(signInRequest));
 }
       
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestBody OtpVerificationRequest request) {
        String email = request.getEmail();
        String enteredOtp = request.getOtp();
    
        String otpStatus = otpService.verifyOtp(email, enteredOtp);
    
        switch (otpStatus) {
            case "valid":
                Admin admin=adminRepository.findByEmail(email);
               
                if(admin!=null)
                {
                    admin.setVerified(true);
                    adminRepository.save(admin);
                }
                return "OTP is valid. User is verified!";
            case "invalid":
                return "Invalid OTP. User verification failed.";
            case "expired":
               
                return "OTP has expired. Please request a new OTP.";
            default:
                return "Unexpected OTP status. User verification failed.";
        }
    }

     @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        boolean isResent = otpService.resendOtp(email);

        if (isResent) {
            return ResponseEntity.ok("OTP resent successfully. Check your email for verification.");
        } else {
            return ResponseEntity.badRequest().body("No expired OTP found for the provided email.");
        }
    }
    

    

  

    
   
}
