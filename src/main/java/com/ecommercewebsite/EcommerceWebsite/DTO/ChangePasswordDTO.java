package com.ecommercewebsite.EcommerceWebsite.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor@AllArgsConstructor
public class ChangePasswordDTO {
    private String email;
    private String currentPassword;
    private String newPassword;
}