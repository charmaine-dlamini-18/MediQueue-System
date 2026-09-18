package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class RoleFactoryTest {

    @Test
    void createRole_withValidData_returnsRole() {
        var role = RoleFactory.createRole("1", "Nurse");

        assertNotNull(role);
        assertEquals(1L, role.getRoleId());
        assertEquals("Nurse", role.getRoleName());
    }

    @Test
    void createRole_withNonNumericRoleId_returnsNull() {
        var role = RoleFactory.createRole("R-001", "Nurse");
        assertNull(role);
    }

    @Test
    void createRole_withNullRoleId_returnsNull() {
        var role = RoleFactory.createRole(null, "Nurse");
        assertNull(role);
    }

    @Test
    void createRole_withBlankRoleId_returnsNull() {
        var role = RoleFactory.createRole("   ", "Nurse");
        assertNull(role);
    }

    @Test
    void createRole_withNullRoleName_returnsNull() {
        var role = RoleFactory.createRole("2", null);
        assertNull(role);
    }

    @Test
    void createRole_withBlankRoleName_returnsNull() {
        var role = RoleFactory.createRole("3", "");
        assertNull(role);
    }

    @Test
    void createRole_withOnlyRoleName_returnsRoleWithNullId() {
        var role = RoleFactory.createRole("Doctor");

        assertNotNull(role);
        assertEquals("Doctor", role.getRoleName());
    }

    @Test
    void createRole_withOnlyRoleName_nullRoleName_returnsNull() {
        var role = RoleFactory.createRole((String) null);
        assertNull(role);
    }
}