package com.cput.mediqueuesystem.repository;

import com.cput.mediqueuesystem.domain.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void saveRole_ShouldWork() {
        Role role = new Role();
        role.setRoleName("ADMIN");
        Role saved = roleRepository.save(role);
        assertNotNull(saved);
    }
}
