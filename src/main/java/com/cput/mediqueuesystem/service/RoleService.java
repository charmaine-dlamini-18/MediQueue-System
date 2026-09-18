package com.cput.mediqueuesystem.service;

import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleById(Long id) {
        Optional<Role> role = roleRepository.findById(id);
        return role.orElse(null);
    }

    public Role getRoleByName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElse(null);
    }

    public Role createRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(Long id, Role role) {
        Role existing = getRoleById(id);
        if (existing != null) {
            existing.setRoleName(role.getRoleName());
            existing.setDescription(role.getDescription());
            return roleRepository.save(existing);
        }
        return null;
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public boolean roleExists(Long id) {
        return roleRepository.existsById(id);
    }
}
