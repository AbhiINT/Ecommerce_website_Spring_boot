package com.ecommercewebsite.EcommerceWebsite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.OtpVerificationRequest;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.config.SecuriryService.OtpService;
import com.ecommercewebsite.EcommerceWebsite.entity.Admin;
import com.ecommercewebsite.EcommerceWebsite.entity.User;
import com.ecommercewebsite.EcommerceWebsite.repository.AdminRepository;
import com.ecommercewebsite.EcommerceWebsite.repository.UserRepository;
import com.ecommercewebsite.EcommerceWebsite.service.AdminService;
import com.ecommercewebsite.EcommerceWebsite.service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired 
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;

    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        System.err.println(signUpRequest.toString());
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){

        User user=userRepository.findByEmail(signInRequest.getEmail());
        Admin admin=adminRepository.findByEmail(signInRequest.getEmail());

        if(user!=null){
        if(userRepository.findByEmail(signInRequest.getEmail()).isVerified())
             return ResponseEntity.ok(authService.signInUser(signInRequest));
     
            
        }
        else if(admin!=null)
        {
            if(adminRepository.findByEmail(signInRequest.getEmail()).isVerified())
             return ResponseEntity.ok(authService.signInAdmin(signInRequest));
        }
            ReqRes res=new ReqRes();
            res.setStatusCode(401);
            res.setMessage("User is Not Verifird ! Please Verify to ACtivate account and then try login.");
            return ResponseEntity.ok(res);
        
       
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
                User user = userRepository.findByEmail(email);
                if(user!=null)
                {
                    user.setVerified(true);
                    userRepository.save(user);
                }
                else {
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

    

  

    @PostMapping("/chnage-password")
    public ResponseEntity<ReqRes> changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
        ReqRes response = adminService.changePassword(changePasswordDTO);
        return ResponseEntity.ok(response);
        
    }
    

   
}