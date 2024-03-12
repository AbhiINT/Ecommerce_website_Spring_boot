package com.ecommercewebsite.EcommerceWebsite.auth.userAuth.userAuthService;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommercewebsite.EcommerceWebsite.DTO.ChangePasswordDTO;
import com.ecommercewebsite.EcommerceWebsite.DTO.ReqRes;
import com.ecommercewebsite.EcommerceWebsite.model.user.entity.User;
import com.ecommercewebsite.EcommerceWebsite.model.user.repository.UserRepository;
import com.ecommercewebsite.EcommerceWebsite.remote.otp.service.OtpService;
import com.ecommercewebsite.EcommerceWebsite.util.JWTUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthService {

    private final OtpService otpService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public ReqRes handleSignUpUser(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {

            User existinguser = userRepository.findByEmail(registrationRequest.getEmail());

            if (existinguser != null) {
                if (!existinguser.isVerified()) {

                    boolean isOtpResent = otpService.resendOtp(existinguser.getEmail());

                    if (isOtpResent) {
                        resp.setStatusCode(200);
                        resp.setMessage("Admin exists. Resent OTP for verification. Check your email.");
                    } else {
                        resp.setStatusCode(500);
                        resp.setError("Failed to resend OTP. Please try again.");
                    }
                    return resp;
                } else {

                    resp.setStatusCode(400);
                    resp.setMessage("User with this email already exists.");
                    return resp;
                }
            }

            User ourUsers = new User();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRole(registrationRequest.getRole());
            ourUsers.setFirstName(registrationRequest.getFirstName());
            ourUsers.setLastName(registrationRequest.getLastName());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());

            if (registrationRequest != null && registrationRequest.getPassword() != null) {
                String generatedOtp = otpService.generateOtp();
                boolean isOtpSaved = otpService.saveOtp(registrationRequest.getEmail(), generatedOtp);

                if (isOtpSaved && otpService.sendOtpEmail(registrationRequest.getEmail(), generatedOtp)) {
                    userRepository.save(ourUsers);
                    resp.setMessage("User Registered Successfully. Check your email for OTP verification.");
                    resp.setStatusCode(200);
                } else {
                    resp.setStatusCode(500);
                    resp.setError("Failed to save or send OTP. Please try again.");
                }
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Internal Server Error. Please try again.");

            e.printStackTrace();
        }
        return resp;
    }

    public ReqRes signInUser(ReqRes signinRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

            User user = userRepository.findByEmail(signinRequest.getEmail());

            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("3 MINUTES");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {

            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }

    public ReqRes changePassword(ChangePasswordDTO changePasswordDTO) {
        ReqRes resp = new ReqRes();
        User user = userRepository.findByEmail(changePasswordDTO.getEmail());

        if (user.getPassword().matches(changePasswordDTO.getCurrentPassword())) {
            user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            userRepository.save(user);
            resp.setStatusCode(200);
            resp.setMessage("Password Chnaged Successfully");
            return resp;
        }
        resp.setStatusCode(400);
        resp.setMessage("Wrong Old Password");
        return resp;

    }

    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        User user = userRepository.findByEmail(ourEmail);
        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
            var jwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }

}
