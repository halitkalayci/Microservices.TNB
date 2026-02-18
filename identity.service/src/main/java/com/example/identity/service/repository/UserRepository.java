package com.example.identity.service.repository;

import com.example.identity.service.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    boolean existsByTcknHashed(String tcknHashed);

    Optional<User> findByUsername(String username);

    Optional<User> findByPasswordResetToken(String passwordResetToken);

    // SQL Injection'a açıktır.
    //@Query("SELECT u FROM User u WHERE u.id =" + "':id" +  "'")
    //List<User> findByIdRaw(@Param("id") UUID id);

    // SQL Injection'a kapalıdır.
    //@Query("SELECT u FROM User u WHERE u.id = :id")
    //List<User> findByIdPreparedStatement(@Param("id") UUID id);
}
