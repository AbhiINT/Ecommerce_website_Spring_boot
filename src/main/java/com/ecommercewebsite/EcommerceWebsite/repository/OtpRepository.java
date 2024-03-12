package com.ecommercewebsite.EcommerceWebsite.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommercewebsite.EcommerceWebsite.remote.otp.entity.OtpEntity;

import java.sql.Timestamp;
import java.util.Optional;
public interface OtpRepository extends JpaRepository<OtpEntity, Long> {
    Optional<OtpEntity> findByEmail(String email);

    Optional<OtpEntity> findByEmailAndExpirationTimeAfter(String email, Timestamp timestamp);

    Optional<OtpEntity> findByEmailAndExpirationTimeBefore(String email, Timestamp timestamp);
}
