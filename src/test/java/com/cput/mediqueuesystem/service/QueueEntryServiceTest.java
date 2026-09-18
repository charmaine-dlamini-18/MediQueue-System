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

import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Queue;
import com.cput.mediqueuesystem.domain.QueueEntry;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.repository.QueueEntryRepository;

@ExtendWith(MockitoExtension.class)
class QueueEntryServiceTest {

    @Mock
    private QueueEntryRepository queueEntryRepository;

    @InjectMocks
    private QueueEntryService queueEntryService;

    private QueueEntry validEntry;

    @BeforeEach
    void setUp() {
        Clinic clinic = new Clinic.Builder().setClinicId("C-001").build();
        Queue queue = new Queue.Builder().setQueueId("Q-001").setClinic(clinic).setDate(LocalDate.now()).build();
        Patient patient = new Patient.Builder().setUserId("P-001").build();
        Staff doctor = new Staff.Builder().setUserId("U-001").build();

        validEntry = new QueueEntry.Builder()
                .setQueueEntryId("QE-001").setQueue(queue).setPatient(patient).setDoctor(doctor)
                .setQueueNumber(1).setStatus("Waiting")
                .build();
    }

    @Test
    void create_withValidEntry_savesAndReturnsEntry() {
        when(queueEntryRepository.save(any(QueueEntry.class))).thenReturn(validEntry);

        QueueEntry result = queueEntryService.create(validEntry);

        assertEquals("QE-001", result.getQueueEntryId());
        verify(queueEntryRepository, times(1)).save(any(QueueEntry.class));
    }

    @Test
    void create_withNullEntry_returnsNull() {
        QueueEntry result = queueEntryService.create(null);

        assertNull(result);
        verify(queueEntryRepository, never()).save(any(QueueEntry.class));
    }

    @Test
    void create_withInvalidEntry_failsFactoryValidation_returnsNull() {
        QueueEntry invalid = new QueueEntry.Builder().setQueueEntryId(null).build();

        QueueEntry result = queueEntryService.create(invalid);

        assertNull(result);
        verify(queueEntryRepository, never()).save(any(QueueEntry.class));
    }

    @Test
    void read_withExistingId_returnsEntry() {
        when(queueEntryRepository.findById("QE-001")).thenReturn(Optional.of(validEntry));

        QueueEntry result = queueEntryService.read("QE-001");

        assertEquals("Waiting", result.getStatus());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(queueEntryRepository.findById("QE-999")).thenReturn(Optional.empty());

        QueueEntry result = queueEntryService.read("QE-999");

        assertNull(result);
    }

    @Test
    void update_whenEntryExists_savesAndReturnsUpdated() {
        when(queueEntryRepository.existsById("QE-001")).thenReturn(true);
        when(queueEntryRepository.save(validEntry)).thenReturn(validEntry);

        QueueEntry result = queueEntryService.update(validEntry);

        assertEquals("QE-001", result.getQueueEntryId());
    }

    @Test
    void update_whenEntryDoesNotExist_returnsNull() {
        when(queueEntryRepository.existsById("QE-001")).thenReturn(false);

        QueueEntry result = queueEntryService.update(validEntry);

        assertNull(result);
        verify(queueEntryRepository, never()).save(any(QueueEntry.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        queueEntryService.delete("QE-001");

        verify(queueEntryRepository, times(1)).deleteById("QE-001");
    }

    @Test
    void getAll_returnsAllEntries() {
        when(queueEntryRepository.findAll()).thenReturn(List.of(validEntry));

        List<QueueEntry> result = queueEntryService.getAll();

        assertEquals(1, result.size());
    }
}