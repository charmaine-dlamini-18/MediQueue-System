package com.cput.mediqueuesystem.service;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Visit;
import com.cput.mediqueuesystem.repository.VisitRepository;

@ExtendWith(MockitoExtension.class)
class VisitServiceTest {

    @Mock
    private VisitRepository visitRepository;

    @InjectMocks
    private VisitService visitService;

    private Visit validVisit;
    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        validVisit = new Visit.Builder()
                .setVisitId("V-001")
                .setPatient(patient)
                .setVisitDate(LocalDate.of(2026, 5, 7))
                .setCheckInTime(LocalTime.of(9, 15))
                .setStatus("Pending")
                .build();
    }

    @Test
    void create_withValidVisit_savesAndReturnsVisit() {
        when(visitRepository.save(any(Visit.class))).thenReturn(validVisit);

        Visit result = visitService.create(validVisit);

        assertEquals("V-001", result.getVisitId());
        verify(visitRepository, times(1)).save(any(Visit.class));
    }

    @Test
    void create_withNullVisit_returnsNullAndNeverCallsRepository() {
        Visit result = visitService.create(null);

        assertNull(result);
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    void create_withInvalidVisit_failsFactoryValidation_returnsNull() {
        Visit invalid = new Visit.Builder()
                .setVisitId(null) // invalid - required by factory
                .setPatient(patient)
                .setVisitDate(LocalDate.now())
                .build();

        Visit result = visitService.create(invalid);

        assertNull(result);
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    void read_withExistingId_returnsVisit() {
        when(visitRepository.findById("V-001")).thenReturn(Optional.of(validVisit));

        Visit result = visitService.read("V-001");

        assertEquals("V-001", result.getVisitId());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(visitRepository.findById("V-9999")).thenReturn(Optional.empty());

        Visit result = visitService.read("V-9999");

        assertNull(result);
    }

    @Test
    void update_whenVisitExists_savesAndReturnsUpdatedVisit() {
        when(visitRepository.existsById("V-001")).thenReturn(true);
        when(visitRepository.save(validVisit)).thenReturn(validVisit);

        Visit result = visitService.update(validVisit);

        assertEquals("V-001", result.getVisitId());
        verify(visitRepository, times(1)).save(validVisit);
    }

    @Test
    void update_whenVisitDoesNotExist_returnsNullAndNeverSaves() {
        when(visitRepository.existsById("V-001")).thenReturn(false);

        Visit result = visitService.update(validVisit);

        assertNull(result);
        verify(visitRepository, never()).save(any(Visit.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        visitService.delete("V-001");

        verify(visitRepository, times(1)).deleteById("V-001");
    }

    @Test
    void getAll_returnsAllVisitsFromRepository() {
        when(visitRepository.findAll()).thenReturn(List.of(validVisit));

        List<Visit> result = visitService.getAll();

        assertEquals(1, result.size());
        assertEquals("V-001", result.get(0).getVisitId());
    }

    @Test
    void getAll_whenNoVisits_returnsEmptyList() {
        when(visitRepository.findAll()).thenReturn(List.of());

        List<Visit> result = visitService.getAll();

        assertEquals(0, result.size());
    }
}