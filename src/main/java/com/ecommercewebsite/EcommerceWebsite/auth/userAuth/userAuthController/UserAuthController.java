package com.ecommercewebsite.EcommerceWebsite.auth.userAuth.userAuthController;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.OtpVerificationRequest;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.admin.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.auth.userAuth.userAuthService.UserAuthService;
import com.ecommercewebsite.EcommerceWebsite.entity.User;
import com.ecommercewebsite.EcommerceWebsite.remote.service.OtpService;
import com.ecommercewebsite.EcommerceWebsite.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/user")
public class UserAuthController {
    private final OtpService otpService;
    private final UserAuthService authService;
    private final UserRepository userRepository;
   
    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes registrationRequest){
      
        return ResponseEntity.ok(authService.handleSignUpUser(registrationRequest));
    }

    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){

       
       
             return ResponseEntity.ok(authService.signInUser(signInRequest));
     
            
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
                User user=userRepository.findByEmail(email);
               
                if(user!=null)
                {
                    user.setVerified(true);
                    userRepository.save(user);
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
    

    

  

    @PostMapping("/chnage-password")
    public ResponseEntity<ReqRes> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        ReqRes response = authService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(response);
        
    }
}
