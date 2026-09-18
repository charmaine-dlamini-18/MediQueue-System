package com.cput.mediqueuesystem.service;


import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cput.mediqueuesystem.domain.MedicalRecord;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.repository.MedicalRecordRepository;

@ExtendWith(MockitoExtension.class)
class MedicalRecordServiceTest {

    @Mock
    private MedicalRecordRepository medicalRecordRepository;

    @InjectMocks
    private MedicalRecordService medicalRecordService;

    private MedicalRecord validRecord;

    @BeforeEach
    void setUp() {
        Patient patient = new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build();
        Staff staff = new Staff.Builder().setUserId("U-001").build();

        validRecord = new MedicalRecord.Builder()
                .setRecordId("MR-001").setPatient(patient).setCreatedBy(staff)
                .setDiagnosis("Flu").setNotes("Rest advised")
                .setRecordDate(LocalDate.of(2026, 5, 7))
                .build();
    }

    @Test
    void create_withValidRecord_savesAndReturnsRecord() {
        when(medicalRecordRepository.save(any(MedicalRecord.class))).thenReturn(validRecord);

        MedicalRecord result = medicalRecordService.create(validRecord);

        assertEquals("MR-001", result.getRecordId());
        verify(medicalRecordRepository, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void create_withNullRecord_returnsNull() {
        MedicalRecord result = medicalRecordService.create(null);

        assertNull(result);
        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void create_withInvalidRecord_failsFactoryValidation_returnsNull() {
        MedicalRecord invalid = new MedicalRecord.Builder()
                .setRecordId(null).setPatient(new Patient.Builder().setCreatedAt(LocalDateTime.now()).setUserId("P-001").build())
                .setCreatedBy(new Staff.Builder().setUserId("U-001").build())
                .setRecordDate(LocalDate.now())
                .build();

        MedicalRecord result = medicalRecordService.create(invalid);

        assertNull(result);
        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void read_withExistingId_returnsRecord() {
        when(medicalRecordRepository.findById("MR-001")).thenReturn(Optional.of(validRecord));

        MedicalRecord result = medicalRecordService.read("MR-001");

        assertEquals("Flu", result.getDiagnosis());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(medicalRecordRepository.findById("MR-999")).thenReturn(Optional.empty());

        MedicalRecord result = medicalRecordService.read("MR-999");

        assertNull(result);
    }

    @Test
    void update_whenRecordExists_savesAndReturnsUpdated() {
        when(medicalRecordRepository.existsById("MR-001")).thenReturn(true);
        when(medicalRecordRepository.save(validRecord)).thenReturn(validRecord);

        MedicalRecord result = medicalRecordService.update(validRecord);

        assertEquals("MR-001", result.getRecordId());
    }

    @Test
    void update_whenRecordDoesNotExist_returnsNull() {
        when(medicalRecordRepository.existsById("MR-001")).thenReturn(false);

        MedicalRecord result = medicalRecordService.update(validRecord);

        assertNull(result);
        verify(medicalRecordRepository, never()).save(any(MedicalRecord.class));
    }

    @Test
    void delete_whenRecordExists_deletesAndReturnsTrue() {
        when(medicalRecordRepository.existsById("MR-001")).thenReturn(true);

        boolean result = medicalRecordService.delete("MR-001");

        assertEquals(true, result);
        verify(medicalRecordRepository, times(1)).deleteById("MR-001");
    }

    @Test
    void delete_whenRecordDoesNotExist_returnsFalse() {
        when(medicalRecordRepository.existsById("MR-999")).thenReturn(false);

        boolean result = medicalRecordService.delete("MR-999");

        assertEquals(false, result);
        verify(medicalRecordRepository, never()).deleteById(any());
    }

    @Test
    void getAll_returnsAllRecords() {
        when(medicalRecordRepository.findAll()).thenReturn(List.of(validRecord));

        List<MedicalRecord> result = medicalRecordService.getAll();

        assertEquals(1, result.size());
    }
}