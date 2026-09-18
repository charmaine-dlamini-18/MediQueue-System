package com.cput.mediqueuesystem.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import com.cput.mediqueuesystem.domain.SymptomsAnalysis;
import com.cput.mediqueuesystem.repository.SymptomsAnalysisRepository;

@ExtendWith(MockitoExtension.class)
class SymptomsAnalysisServiceTest {

    @Mock
    private SymptomsAnalysisRepository symptomsAnalysisRepository;

    @InjectMocks
    private SymptomsAnalysisService symptomsAnalysisService;

    private SymptomsAnalysis validAnalysis;

    @BeforeEach
    void setUp() {
        Patient patient = new Patient.Builder().setUserId("P-001").build();
        validAnalysis = new SymptomsAnalysis.Builder()
                .setAnalysisId("SA-001").setPatient(patient)
                .setInputText("Headache and fever").setPredictedConditions("Flu")
                .setConfidenceScore(0.85).setCreatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void create_withValidAnalysis_savesAndReturnsAnalysis() {
        when(symptomsAnalysisRepository.save(any(SymptomsAnalysis.class))).thenReturn(validAnalysis);

        SymptomsAnalysis result = symptomsAnalysisService.create(validAnalysis);

        assertEquals("SA-001", result.getAnalysisId());
        verify(symptomsAnalysisRepository, times(1)).save(any(SymptomsAnalysis.class));
    }

    @Test
    void create_withNullAnalysis_returnsNull() {
        SymptomsAnalysis result = symptomsAnalysisService.create(null);

        assertNull(result);
        verify(symptomsAnalysisRepository, never()).save(any(SymptomsAnalysis.class));
    }

    @Test
    void create_withInvalidAnalysis_failsFactoryValidation_returnsNull() {
        SymptomsAnalysis invalid = new SymptomsAnalysis.Builder().setAnalysisId(null).build();

        SymptomsAnalysis result = symptomsAnalysisService.create(invalid);

        assertNull(result);
        verify(symptomsAnalysisRepository, never()).save(any(SymptomsAnalysis.class));
    }

    @Test
    void read_withExistingId_returnsAnalysis() {
        when(symptomsAnalysisRepository.findById("SA-001")).thenReturn(Optional.of(validAnalysis));

        SymptomsAnalysis result = symptomsAnalysisService.read("SA-001");

        assertEquals("Flu", result.getPredictedConditions());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(symptomsAnalysisRepository.findById("SA-999")).thenReturn(Optional.empty());

        SymptomsAnalysis result = symptomsAnalysisService.read("SA-999");

        assertNull(result);
    }

    @Test
    void update_whenAnalysisExists_savesAndReturnsUpdated() {
        when(symptomsAnalysisRepository.existsById("SA-001")).thenReturn(true);
        when(symptomsAnalysisRepository.save(validAnalysis)).thenReturn(validAnalysis);

        SymptomsAnalysis result = symptomsAnalysisService.update(validAnalysis);

        assertEquals("SA-001", result.getAnalysisId());
    }

    @Test
    void update_whenAnalysisDoesNotExist_returnsNull() {
        when(symptomsAnalysisRepository.existsById("SA-001")).thenReturn(false);

        SymptomsAnalysis result = symptomsAnalysisService.update(validAnalysis);

        assertNull(result);
        verify(symptomsAnalysisRepository, never()).save(any(SymptomsAnalysis.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        symptomsAnalysisService.delete("SA-001");

        verify(symptomsAnalysisRepository, times(1)).deleteById("SA-001");
    }

    @Test
    void getAll_returnsAllAnalyses() {
        when(symptomsAnalysisRepository.findAll()).thenReturn(List.of(validAnalysis));

        List<SymptomsAnalysis> result = symptomsAnalysisService.getAll();

        assertEquals(1, result.size());
    }
}