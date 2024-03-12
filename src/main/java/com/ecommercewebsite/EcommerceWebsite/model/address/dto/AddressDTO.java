package com.ecommercewebsite.EcommerceWebsite.model.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    @NotBlank(message = "Please Specify the Locality . It cannot be blank.")
    @NotNull(message = "Locality cannot be null")
    private String locality;

    @NotBlank(message = "Please Specify the City . It cannot be blank.")
    @NotNull(message = "City cannot be null")
    private String city;

    @NotBlank(message = "Please Specify the District . It cannot be blank.")
    @NotNull(message = "District cannot be null")
    private String district;

    @NotBlank(message = "Please Specify the District . It cannot be blank.")
    @NotNull(message = "District cannot be null")
    private String state;

    @NotBlank(message = "Please Specify the Pin COde . It cannot be blank.")
    @NotNull(message = "Pincode cannot be null")
    @Size(min = 6 , max = 6)
    private Long pincode;
}
