package com.tde.apiVallartaRecargas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tde.apiVallartaRecargas.entity.User;

import java.util.Optional;

public interface OpeUserRepository extends JpaRepository<User, Long> {

    @Query(
        value = "SELECT * " +
                "FROM ope_user " +
                "WHERE [user] = :username " +
                "AND CONVERT(VARCHAR(200), DECRYPTBYPASSPHRASE('viaxerMTY', [password])) = :password",
        nativeQuery = true
    )
    Optional<User> login(
            @Param("username") String username,
            @Param("password") String password
    );
}