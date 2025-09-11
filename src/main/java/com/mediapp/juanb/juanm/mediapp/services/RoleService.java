package com.mediapp.juanb.juanm.mediapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mediapp.juanb.juanm.mediapp.entities.Role;
import com.mediapp.juanb.juanm.mediapp.repositories.RoleRepository;

@Service
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Optional<Role> findByCedula(Long id) {
        return roleRepository.findById(id);
    }

    public Role save(Role role) {
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    public Role update(Long id, Role role) {
        role.setIdRole(id);
        return roleRepository.save(role);
    }
}
