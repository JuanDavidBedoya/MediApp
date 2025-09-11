package com.mediapp.juanb.juanm.mediapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mediapp.juanb.juanm.mediapp.entities.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

}
