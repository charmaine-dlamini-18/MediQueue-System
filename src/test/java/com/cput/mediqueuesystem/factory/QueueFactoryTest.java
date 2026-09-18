package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import com.cput.mediqueuesystem.domain.Clinic;

class QueueFactoryTest {

    private Clinic clinic() {
        return new Clinic.Builder().setClinicId("C-001").setClinicName("District Six Clinic").build();
    }

    @Test
    void createQueue_withValidData_returnsQueue() {
        var queue = QueueFactory.createQueue("Q-001", clinic(), LocalDate.of(2026, 5, 7), 30);

        assertNotNull(queue);
        assertEquals("Q-001", queue.getQueueId());
        assertEquals(30, queue.getMaxCapacity());
    }

    @Test
    void createQueue_withNullQueueId_returnsNull() {
        var queue = QueueFactory.createQueue(null, clinic(), LocalDate.now(), 30);
        assertNull(queue);
    }

    @Test
    void createQueue_withBlankQueueId_returnsNull() {
        var queue = QueueFactory.createQueue("  ", clinic(), LocalDate.now(), 30);
        assertNull(queue);
    }

    @Test
    void createQueue_withNullClinic_returnsNull() {
        var queue = QueueFactory.createQueue("Q-002", null, LocalDate.now(), 30);
        assertNull(queue);
    }

    @Test
    void createQueue_withNullDate_returnsNull() {
        var queue = QueueFactory.createQueue("Q-003", clinic(), null, 30);
        assertNull(queue);
    }

    @Test
    void createQueue_withZeroMaxCapacity_stillReturnsQueue() {
        // maxCapacity is a primitive int - not validated as required by the factory
        var queue = QueueFactory.createQueue("Q-004", clinic(), LocalDate.now(), 0);
        assertNotNull(queue);
    }
}