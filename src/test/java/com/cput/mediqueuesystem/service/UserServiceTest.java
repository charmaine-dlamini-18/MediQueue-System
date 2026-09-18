package com.cput.mediqueuesystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.domain.User;
import com.cput.mediqueuesystem.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // User is abstract - use the Patient subclass to exercise User behaviour
        Role role = new Role.Builder().setRoleId(1L).setRoleName("Patient").build();
        user = new Patient.Builder()
                .setUserId("U-001").setFirstName("Ellen").setLastName("Luella")
                .setEmail("ellen@example.com").setPassword("pass123")
                .setPhoneNumber("0821234567").setStatus(true)
                .setCreatedAt(LocalDateTime.now()).setRole(role)
                .setIdNumber("9001015800081").setDateOfBirth(java.time.LocalDate.of(1990, 1, 1))
                .setGender("Female").setAddress("123 Main Street")
                .build();
    }

    @Test
    void create_savesAndReturnsUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.create(user);

        assertEquals("U-001", result.getUserId());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void read_withExistingId_returnsUser() {
        when(userRepository.findById("U-001")).thenReturn(Optional.of(user));

        User result = userService.read("U-001");

        assertEquals("Ellen", result.getFirstName());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(userRepository.findById("U-999")).thenReturn(Optional.empty());

        User result = userService.read("U-999");

        assertNull(result);
    }

    @Test
    void update_savesAndReturnsUser() {
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.update(user);

        assertEquals("U-001", result.getUserId());
    }

    @Test
    void delete_callsRepositoryDeleteById_andReturnsTrue() {
        boolean result = userService.delete("U-001");

        assertEquals(true, result);
        verify(userRepository, times(1)).deleteById("U-001");
    }

    @Test
    void getAll_returnsAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<User> result = userService.getAll();

        assertEquals(1, result.size());
    }
}