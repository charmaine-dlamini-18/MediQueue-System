package com.cput.mediqueuesystem.repository;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cput.mediqueuesystem.domain.Queue;

/*
 * QueueRepository.java
 * JPA repository for Queue entities.
 *
 * Author: Uya
 * Date: 03 August 2026
 */

@Repository
public interface QueueRepository extends JpaRepository<Queue, String> {

    Optional<Queue> findByClinicClinicIdAndDate(String clinicId, LocalDate date);
}
