package com.ecommercewebsite.EcommerceWebsite.model.admin.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ecommercewebsite.EcommerceWebsite.model.roles.Roles.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class Admin implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First Name Cannot be blank")
    @NotNull(message = "First Name Cannot be Null")
    private String firstName;

    @NotBlank(message = "Last Name Cannot be blank")
    @NotNull(message = "Last Name Cannot be Null")
    private String lastName;

    @Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    @NotBlank(message = "Password Cannot be Blank")
    @Size(min = 8, message = "Password must be between 8 and 16 characters")
    private String password;

    @NotNull(message = "Please Enter Phone Number It cannot be null")
    @Size(min = 10, max = 10, message = "Phone Number must be 10 digits")
    private String phoneNumber;

    
    private Role role=Role.ADMIN;;

    private boolean isVerified = false;

   

    

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String roleString = role.toString();
        return List.of(new SimpleGrantedAuthority(roleString));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
