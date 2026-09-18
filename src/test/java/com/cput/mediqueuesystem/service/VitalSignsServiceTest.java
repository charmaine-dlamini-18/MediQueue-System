package com.cput.mediqueuesystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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
import com.cput.mediqueuesystem.domain.VitalSigns;
import com.cput.mediqueuesystem.domain.Visit;
import com.cput.mediqueuesystem.repository.VitalSignsRepository;

@ExtendWith(MockitoExtension.class)
class VitalSignsServiceTest {

    @Mock
    private VitalSignsRepository vitalSignsRepository;

    @InjectMocks
    private VitalSignsService vitalSignsService;

    private VitalSigns validVitals;
    private Visit visit;

    @BeforeEach
    void setUp() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        visit = new Visit.Builder()
                .setVisitId("V-001")
                .setPatient(patient)
                .setVisitDate(LocalDate.of(2026, 5, 7))
                .setStatus("Pending")
                .build();

        validVitals = new VitalSigns.Builder()
                .setVitalId("VS-0001")
                .setVisit(visit)
                .setTemperature("36.8")
                .setBloodPressure("117/76")
                .setHeartRate("78")
                .setWeight("80")
                .setRecordedAt(LocalDateTime.of(2026, 5, 6, 9, 15))
                .build();
    }

    @Test
    void create_withValidVitalSigns_savesAndReturnsVitalSigns() {
        when(vitalSignsRepository.save(any(VitalSigns.class))).thenReturn(validVitals);

        VitalSigns result = vitalSignsService.create(validVitals);

        assertEquals("VS-0001", result.getVitalId());
        verify(vitalSignsRepository, times(1)).save(any(VitalSigns.class));
    }

    @Test
    void create_withNullVitalSigns_returnsNullAndNeverCallsRepository() {
        VitalSigns result = vitalSignsService.create(null);

        assertNull(result);
        verify(vitalSignsRepository, never()).save(any(VitalSigns.class));
    }

    @Test
    void create_withNullVisit_failsFactoryValidation_returnsNull() {
        VitalSigns invalid = new VitalSigns.Builder()
                .setVitalId("VS-0002")
                .setVisit(null) // invalid - required by factory
                .setTemperature("36.8")
                .build();

        VitalSigns result = vitalSignsService.create(invalid);

        assertNull(result);
        verify(vitalSignsRepository, never()).save(any(VitalSigns.class));
    }

    @Test
    void read_withExistingId_returnsVitalSigns() {
        when(vitalSignsRepository.findById("VS-0001")).thenReturn(Optional.of(validVitals));

        VitalSigns result = vitalSignsService.read("VS-0001");

        assertEquals("VS-0001", result.getVitalId());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(vitalSignsRepository.findById("VS-9999")).thenReturn(Optional.empty());

        VitalSigns result = vitalSignsService.read("VS-9999");

        assertNull(result);
    }

    @Test
    void update_whenVitalSignsExist_savesAndReturnsUpdated() {
        when(vitalSignsRepository.existsById("VS-0001")).thenReturn(true);
        when(vitalSignsRepository.save(validVitals)).thenReturn(validVitals);

        VitalSigns result = vitalSignsService.update(validVitals);

        assertEquals("VS-0001", result.getVitalId());
        verify(vitalSignsRepository, times(1)).save(validVitals);
    }

    @Test
    void update_whenVitalSignsDoNotExist_returnsNullAndNeverSaves() {
        when(vitalSignsRepository.existsById("VS-0001")).thenReturn(false);

        VitalSigns result = vitalSignsService.update(validVitals);

        assertNull(result);
        verify(vitalSignsRepository, never()).save(any(VitalSigns.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        vitalSignsService.delete("VS-0001");

        verify(vitalSignsRepository, times(1)).deleteById("VS-0001");
    }

    @Test
    void getAll_returnsAllVitalSignsFromRepository() {
        when(vitalSignsRepository.findAll()).thenReturn(List.of(validVitals));

        List<VitalSigns> result = vitalSignsService.getAll();

        assertEquals(1, result.size());
        assertEquals("VS-0001", result.get(0).getVitalId());
    }

    @Test
    void getAll_whenNoVitalSigns_returnsEmptyList() {
        when(vitalSignsRepository.findAll()).thenReturn(List.of());

        List<VitalSigns> result = vitalSignsService.getAll();

        assertEquals(0, result.size());
    }
}