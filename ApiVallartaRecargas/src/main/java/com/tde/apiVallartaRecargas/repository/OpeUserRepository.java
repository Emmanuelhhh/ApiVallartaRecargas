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
    
    @Query(value = 
    	    "SELECT A.* "+
    	    "FROM ope_user A "+
    	    "INNER JOIN ope_permisos_reportes B "+
    	        "ON B.id_user = A.id " +
    	       "AND B.id_rep = 7 "+
    	       "AND B.[status] = '1' " +
    	    "WHERE A.[user] = :username " +
    	      "AND CONVERT(VARCHAR(200), DECRYPTBYPASSPHRASE('viaxerMTY', A.[password])) "+ 
    	          "COLLATE Latin1_General_CS_AS = :password "+
    	      "AND A.[status] = 'ACTIVO'",
    	    nativeQuery = true)
    	Optional<User> loginConPermiso(
    	        @Param("username") String username,
    	        @Param("password") String password
    	);
}