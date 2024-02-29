package com.learning.dscatalog.repositories;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.learning.dscatalog.entities.PasswordRecover;

public interface PasswordRecoverRepository extends JpaRepository<PasswordRecover, Long> {

    @Query(value = "SELECT obj FROM PasswordRecover obj WHERE obj.token = :token AND obj.tokenExpiration > :now")
    List<PasswordRecover> searchValidToken(String token, Instant now);

}
