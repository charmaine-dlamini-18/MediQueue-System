package com.cput.mediqueuesystem.repository;

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

import com.cput.mediqueuesystem.domain.Clinic;
import com.cput.mediqueuesystem.domain.Queue;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class QueueRepositoryTest {

    @Autowired
    private QueueRepository queueRepository;

    @Autowired
    private ClinicRepository clinicRepository;

    private Clinic clinic;

    @BeforeEach
    void setUp() {
        clinic = clinicRepository.save(new Clinic.Builder()
                .setClinicId("C-001").setClinicName("District Six Clinic").build());
    }

    @Test
    void save_persistsQueue() {
        Queue queue = new Queue.Builder()
                .setQueueId("Q-001").setClinic(clinic).setDate(LocalDate.of(2026, 5, 7)).setMaxCapacity(30).build();

        Queue saved = queueRepository.save(queue);

        assertEquals("Q-001", saved.getQueueId());
    }

    @Test
    void findById_whenQueueExists_returnsQueue() {
        queueRepository.save(new Queue.Builder()
                .setQueueId("Q-002").setClinic(clinic).setDate(LocalDate.now()).setMaxCapacity(20).build());

        Optional<Queue> found = queueRepository.findById("Q-002");

        assertTrue(found.isPresent());
        assertEquals(20, found.get().getMaxCapacity());
    }

    @Test
    void findById_whenQueueDoesNotExist_returnsEmpty() {
        assertFalse(queueRepository.findById("Q-999").isPresent());
    }

    @Test
    void existsById_afterSave_returnsTrue() {
        queueRepository.save(new Queue.Builder()
                .setQueueId("Q-003").setClinic(clinic).setDate(LocalDate.now()).setMaxCapacity(15).build());

        assertTrue(queueRepository.existsById("Q-003"));
    }

    @Test
    void deleteById_removesQueue() {
        queueRepository.save(new Queue.Builder()
                .setQueueId("Q-004").setClinic(clinic).setDate(LocalDate.now()).setMaxCapacity(15).build());

        queueRepository.deleteById("Q-004");

        assertFalse(queueRepository.existsById("Q-004"));
    }

    @Test
    void findAll_returnsAllSavedQueues() {
        queueRepository.save(new Queue.Builder()
                .setQueueId("Q-005").setClinic(clinic).setDate(LocalDate.now()).setMaxCapacity(25).build());
        queueRepository.save(new Queue.Builder()
                .setQueueId("Q-006").setClinic(clinic).setDate(LocalDate.now()).setMaxCapacity(25).build());

        assertEquals(2, queueRepository.findAll().size());
    }
}