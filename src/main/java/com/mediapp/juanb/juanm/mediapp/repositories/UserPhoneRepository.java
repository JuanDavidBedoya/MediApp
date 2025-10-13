package com.mediapp.juanb.juanm.mediapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import com.mediapp.juanb.juanm.mediapp.entities.Phone;
import com.mediapp.juanb.juanm.mediapp.entities.User;
import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;

@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, UUID> {

    boolean existsByUserAndPhone(User user, Phone phone);
    Optional<UserPhone> findByUserAndPhone(User user, Phone phone);

}
