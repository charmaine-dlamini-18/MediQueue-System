package com.cput.mediqueuesystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cput.mediqueuesystem.domain.User;

/*
 * UserRepository.java
 * JPA repository for User entities (all user types).
 *
 * Author: Charmaine Dlamini
 * Date: 05 August 2026
 */

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    // Inherits CRUD operations (create, read, update, delete) from JpaRepository

    Optional<User> findByEmail(String email);
}
