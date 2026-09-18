package com.cput.mediqueuesystem.repository;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.User;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private Role role;

    @BeforeEach
    void setUp() {
        role = roleRepository.save(new Role.Builder().setRoleId(1L).setRoleName("Patient").build());
    }

    // User is abstract, so all persistence tests go through the Patient subclass -
    // UserRepository just inherits the CRUD methods from JpaRepository<User, String>.
    private Patient buildUser(String userId) {
        return new Patient.Builder().setCreatedAt(LocalDateTime.now())
                .setUserId(userId).setFirstName("Ellen").setLastName("Luella")
                .setEmail(userId + "@example.com").setPassword("pass123")
                .setPhoneNumber("0821234567").setStatus(true).setRole(role)
                .setIdNumber("900101580008" + userId.charAt(userId.length() - 1))
                .setDateOfBirth(LocalDate.of(1990, 1, 1)).setGender("Female")
                .setAddress("123 Main Street")
                .build();
    }

    @Test
    void save_persistsUser() {
        User saved = userRepository.save(buildUser("U1"));

        assertEquals("U1", saved.getUserId());
    }

    @Test
    void findById_whenUserExists_returnsUser() {
        userRepository.save(buildUser("U2"));

        Optional<User> found = userRepository.findById("U2");

        assertTrue(found.isPresent());
    }

    @Test
    void findById_whenUserDoesNotExist_returnsEmpty() {
        assertFalse(userRepository.findById("U-999").isPresent());
    }

    @Test
    void existsById_afterSave_returnsTrue() {
        userRepository.save(buildUser("U3"));

        assertTrue(userRepository.existsById("U3"));
    }

    @Test
    void deleteById_removesUser() {
        userRepository.save(buildUser("U4"));

        userRepository.deleteById("U4");

        assertFalse(userRepository.existsById("U4"));
    }

    @Test
    void findAll_returnsAllSavedUsers() {
        userRepository.save(buildUser("U5"));
        userRepository.save(buildUser("U6"));

        assertEquals(2, userRepository.findAll().size());
    }
}
