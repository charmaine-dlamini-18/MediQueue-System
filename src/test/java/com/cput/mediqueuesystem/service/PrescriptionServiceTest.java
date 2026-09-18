package com.cput.mediqueuesystem.service;

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
import com.cput.mediqueuesystem.domain.Prescription;
import com.cput.mediqueuesystem.repository.PrescriptionRepository;

@ExtendWith(MockitoExtension.class)
class PrescriptionServiceTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @InjectMocks
    private PrescriptionService prescriptionService;

    private Prescription validPrescription;

    @BeforeEach
    void setUp() {
        MedicalRecord record = new MedicalRecord.Builder().setRecordId("MR-001").build();

        validPrescription = new Prescription.Builder()
                .setPrescriptionId("RX-001").setMedicalRecord(record)
                .setMedicationName("Paracetamol").setDosage("500mg")
                .setInstructions("Twice daily").setPrescriptionDate(LocalDate.of(2026, 5, 7))
                .build();
    }

    @Test
    void create_withValidPrescription_savesAndReturnsPrescription() {
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(validPrescription);

        Prescription result = prescriptionService.create(validPrescription);

        assertEquals("RX-001", result.getPrescriptionId());
        verify(prescriptionRepository, times(1)).save(any(Prescription.class));
    }

    @Test
    void create_withNullPrescription_returnsNull() {
        Prescription result = prescriptionService.create(null);

        assertNull(result);
        verify(prescriptionRepository, never()).save(any(Prescription.class));
    }

    @Test
    void create_withInvalidPrescription_failsFactoryValidation_returnsNull() {
        Prescription invalid = new Prescription.Builder()
                .setPrescriptionId(null)
                .setMedicalRecord(new MedicalRecord.Builder().setRecordId("MR-001").build())
                .setMedicationName("Paracetamol").setPrescriptionDate(LocalDate.now())
                .build();

        Prescription result = prescriptionService.create(invalid);

        assertNull(result);
        verify(prescriptionRepository, never()).save(any(Prescription.class));
    }

    @Test
    void read_withExistingId_returnsPrescription() {
        when(prescriptionRepository.findById("RX-001")).thenReturn(Optional.of(validPrescription));

        Prescription result = prescriptionService.read("RX-001");

        assertEquals("Paracetamol", result.getMedicationName());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(prescriptionRepository.findById("RX-999")).thenReturn(Optional.empty());

        Prescription result = prescriptionService.read("RX-999");

        assertNull(result);
    }

    @Test
    void update_whenPrescriptionExists_savesAndReturnsUpdated() {
        when(prescriptionRepository.existsById("RX-001")).thenReturn(true);
        when(prescriptionRepository.save(validPrescription)).thenReturn(validPrescription);

        Prescription result = prescriptionService.update(validPrescription);

        assertEquals("RX-001", result.getPrescriptionId());
    }

    @Test
    void update_whenPrescriptionDoesNotExist_returnsNull() {
        when(prescriptionRepository.existsById("RX-001")).thenReturn(false);

        Prescription result = prescriptionService.update(validPrescription);

        assertNull(result);
        verify(prescriptionRepository, never()).save(any(Prescription.class));
    }

    @Test
    void delete_whenPrescriptionExists_deletesAndReturnsTrue() {
        when(prescriptionRepository.existsById("RX-001")).thenReturn(true);

        boolean result = prescriptionService.delete("RX-001");

        assertEquals(true, result);
        verify(prescriptionRepository, times(1)).deleteById("RX-001");
    }

    @Test
    void delete_whenPrescriptionDoesNotExist_returnsFalse() {
        when(prescriptionRepository.existsById("RX-999")).thenReturn(false);

        boolean result = prescriptionService.delete("RX-999");

        assertEquals(false, result);
        verify(prescriptionRepository, never()).deleteById(any());
    }

    @Test
    void getAll_returnsAllPrescriptions() {
        when(prescriptionRepository.findAll()).thenReturn(List.of(validPrescription));

        List<Prescription> result = prescriptionService.getAll();

        assertEquals(1, result.size());
    }
}