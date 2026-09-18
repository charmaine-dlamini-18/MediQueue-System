package com.cput.mediqueuesystem.repository;

import com.cput.mediqueuesystem.domain.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Test
    void saveDepartment_ShouldWork() {
        // Department uses String for ID
        Department dept = new Department.Builder()
                .setDepartmentId("D-001")
                .setDepartmentName("Cardiology")
                .build();

        Department saved = departmentRepository.save(dept);
        assertNotNull(saved);
        assertNotNull(saved.getDepartmentId());
    }
}
