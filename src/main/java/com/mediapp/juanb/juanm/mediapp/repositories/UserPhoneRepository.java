package com.mediapp.juanb.juanm.mediapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

import com.mediapp.juanb.juanm.mediapp.entities.UserPhone;

@Repository
public interface UserPhoneRepository extends JpaRepository<UserPhone, UUID> {

}
