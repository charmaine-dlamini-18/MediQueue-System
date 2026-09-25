package com.cput.mediqueuesystem.factory;

import java.time.LocalTime;

import org.apache.commons.validator.GenericValidator;

import com.cput.mediqueuesystem.domain.Patient;
import com.cput.mediqueuesystem.domain.Queue;
import com.cput.mediqueuesystem.domain.QueueEntry;
import com.cput.mediqueuesystem.domain.Staff;
import com.cput.mediqueuesystem.domain.Visit;

/*
 * QueueEntryFactory.java
 * Validates input and builds QueueEntry objects.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

public class QueueEntryFactory {

    public static QueueEntry createQueueEntry(String queueEntryId, Queue queue, Patient patient,
                                               Staff doctor, Visit visit, int queueNumber,
                                               String priorityLevel, String status, LocalTime checkInTime) {

        // A doctor is optional: patients get a queue number when they book,
        // before admin/reception assigns a doctor to the appointment.
        if (GenericValidator.isBlankOrNull(queueEntryId) || queue == null || patient == null) {
            return null;
        }

        return new QueueEntry.Builder()
                .setQueueEntryId(queueEntryId)
                .setQueue(queue)
                .setPatient(patient)
                .setDoctor(doctor)
                .setVisit(visit)
                .setQueueNumber(queueNumber)
                .setPriorityLevel(priorityLevel)
                .setStatus(status)
                .setCheckInTime(checkInTime)
                .build();
    }
}
