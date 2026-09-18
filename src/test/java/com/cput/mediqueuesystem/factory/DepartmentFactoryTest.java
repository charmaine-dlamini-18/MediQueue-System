package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DepartmentFactoryTest {

    @Test
    void createDepartment_withValidData_returnsDepartment() {
        var department = DepartmentFactory.createDepartment("D-001", "Cardiology", "Heart care unit");

        assertNotNull(department);
        assertEquals("D-001", department.getDepartmentId());
        assertEquals("Cardiology", department.getDepartmentName());
        assertEquals("Heart care unit", department.getDescription());
    }

    @Test
    void createDepartment_withNullDepartmentId_returnsNull() {
        var department = DepartmentFactory.createDepartment(null, "Cardiology", "desc");
        assertNull(department);
    }

    @Test
    void createDepartment_withBlankDepartmentId_returnsNull() {
        var department = DepartmentFactory.createDepartment("  ", "Cardiology", "desc");
        assertNull(department);
    }

    @Test
    void createDepartment_withNullDepartmentName_returnsNull() {
        var department = DepartmentFactory.createDepartment("D-002", null, "desc");
        assertNull(department);
    }

    @Test
    void createDepartment_withNullDescription_stillReturnsDepartment() {
        // description is optional
        var department = DepartmentFactory.createDepartment("D-003", "Paediatrics", null);
        assertNotNull(department);
    }
}