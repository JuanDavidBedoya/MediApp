package com.mediapp.juanb.juanm.mediapp.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByCedula(String cedula);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.cedula != :cedula")
    boolean existsByEmailAndCedulaNot(@Param("email") String email, @Param("cedula") String cedula);

    List<User> findByEps_Name(String epsName);

    List<User> findByCity_Name(String cityName);

}
