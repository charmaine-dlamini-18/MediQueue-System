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
import com.cput.mediqueuesystem.domain.Queue;
import com.cput.mediqueuesystem.repository.QueueRepository;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    private Queue validQueue;

    @BeforeEach
    void setUp() {
        Clinic clinic = new Clinic.Builder().setClinicId("C-001").setClinicName("District Six Clinic").build();
        validQueue = new Queue.Builder()
                .setQueueId("Q-001").setClinic(clinic)
                .setDate(LocalDate.of(2026, 5, 7)).setMaxCapacity(30)
                .build();
    }

    @Test
    void create_withValidQueue_savesAndReturnsQueue() {
        when(queueRepository.save(any(Queue.class))).thenReturn(validQueue);

        Queue result = queueService.create(validQueue);

        assertEquals("Q-001", result.getQueueId());
        verify(queueRepository, times(1)).save(any(Queue.class));
    }

    @Test
    void create_withNullQueue_returnsNull() {
        Queue result = queueService.create(null);

        assertNull(result);
        verify(queueRepository, never()).save(any(Queue.class));
    }

    @Test
    void create_withInvalidQueue_failsFactoryValidation_returnsNull() {
        Queue invalid = new Queue.Builder().setQueueId(null).setDate(LocalDate.now()).build();

        Queue result = queueService.create(invalid);

        assertNull(result);
        verify(queueRepository, never()).save(any(Queue.class));
    }

    @Test
    void read_withExistingId_returnsQueue() {
        when(queueRepository.findById("Q-001")).thenReturn(Optional.of(validQueue));

        Queue result = queueService.read("Q-001");

        assertEquals(30, result.getMaxCapacity());
    }

    @Test
    void read_withNonExistingId_returnsNull() {
        when(queueRepository.findById("Q-999")).thenReturn(Optional.empty());

        Queue result = queueService.read("Q-999");

        assertNull(result);
    }

    @Test
    void update_whenQueueExists_savesAndReturnsUpdated() {
        when(queueRepository.existsById("Q-001")).thenReturn(true);
        when(queueRepository.save(validQueue)).thenReturn(validQueue);

        Queue result = queueService.update(validQueue);

        assertEquals("Q-001", result.getQueueId());
    }

    @Test
    void update_whenQueueDoesNotExist_returnsNull() {
        when(queueRepository.existsById("Q-001")).thenReturn(false);

        Queue result = queueService.update(validQueue);

        assertNull(result);
        verify(queueRepository, never()).save(any(Queue.class));
    }

    @Test
    void delete_callsRepositoryDeleteById() {
        queueService.delete("Q-001");

        verify(queueRepository, times(1)).deleteById("Q-001");
    }

    @Test
    void getAll_returnsAllQueues() {
        when(queueRepository.findAll()).thenReturn(List.of(validQueue));

        List<Queue> result = queueService.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void getAll_whenEmpty_returnsEmptyList() {
        when(queueRepository.findAll()).thenReturn(List.of());

        List<Queue> result = queueService.getAll();

        assertEquals(0, result.size());
    }
}