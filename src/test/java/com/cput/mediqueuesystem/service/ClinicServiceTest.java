package com.cput.mediqueuesystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.repository.ClinicRepository;

@ExtendWith(MockitoExtension.class)
class ClinicServiceTest {

    @Mock
    private ClinicRepository clinicRepository;

    @InjectMocks
    private ClinicService clinicService;

    private Clinic validClinic;

    @BeforeEach
    void setUp() {
        validClinic = new Clinic.Builder()
                .setClinicId("C-001").setClinicName("District Six Clinic")
                .setLocation("Cape Town").setContactNumber("0211234567").build();
    }

    @Test
    void create_withValidClinic_savesAndReturnsClinic() {
        when(clinicRepository.save(any(Clinic.class))).thenReturn(validClinic);

        Clinic result = clinicService.create(validClinic);

        assertEquals("C-001", result.getClinicId());
        verify(clinicRepository, times(1)).save(any(Clinic.class));
    }

    @Test
    void create_withNullClinic_returnsNull() {
        Clinic result = clinicService.create(null);

        assertNull(result);
        verify(clinicRepository, never()).save(any(Clinic.class));
    }

    @Test
    void create_withInvalidClinic_failsValidation_returnsNull() {
        Clinic invalid = new Clinic.Builder().setClinicId(null).setClinicName("Test").build();

        Clinic result = clinicService.create(invalid);

        assertNull(result);
        verify(clinicRepository, never()).save(any(Clinic.class));
    }

    @Test
    void read_withExistingId_returnsClinic() {
        when(clinicRepository.findById("C-001")).thenReturn(Optional.of(validClinic));

        Clinic result = clinicService.read("C-001");

        assertEquals("District Six Clinic", result.getClinicName());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(clinicRepository.findById("C-999")).thenReturn(Optional.empty());

        Clinic result = clinicService.read("C-999");

        assertNull(result);
    }

    @Test
    void update_whenClinicExists_savesAndReturnsUpdated() {
        when(clinicRepository.existsById("C-001")).thenReturn(true);
        when(clinicRepository.save(validClinic)).thenReturn(validClinic);

        Clinic result = clinicService.update(validClinic);

        assertEquals("C-001", result.getClinicId());
    }

    @Test
    void update_whenClinicDoesNotExist_returnsNull() {
        when(clinicRepository.existsById("C-001")).thenReturn(false);

        Clinic result = clinicService.update(validClinic);

        assertNull(result);
        verify(clinicRepository, never()).save(any(Clinic.class));
    }

    @Test
    void delete_whenClinicExists_deletesAndReturnsTrue() {
        when(clinicRepository.existsById("C-001")).thenReturn(true);

        boolean result = clinicService.delete("C-001");

        assertEquals(true, result);
        verify(clinicRepository, times(1)).deleteById("C-001");
    }

    @Test
    void delete_whenClinicDoesNotExist_returnsFalse() {
        when(clinicRepository.existsById("C-999")).thenReturn(false);

        boolean result = clinicService.delete("C-999");

        assertEquals(false, result);
        verify(clinicRepository, never()).deleteById(any());
    }

    @Test
    void getAll_returnsAllClinics() {
        when(clinicRepository.findAll()).thenReturn(List.of(validClinic));

        List<Clinic> result = clinicService.getAll();

        assertEquals(1, result.size());
    }
}