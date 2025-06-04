package com.odoo.communitypulse.user.repository;

import com.odoo.communitypulse.user.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findByEmailAndCode(String email, String code);
    void deleteByEmail(String email);
}
