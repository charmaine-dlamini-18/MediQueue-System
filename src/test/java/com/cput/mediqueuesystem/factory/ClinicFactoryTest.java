package com.cput.mediqueuesystem.factory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class ClinicFactoryTest {

    @Test
    void createClinic_withValidData_returnsClinic() {
        var clinic = ClinicFactory.createClinic("C-001", "District Six Clinic", "Cape Town", "0211234567");

        assertNotNull(clinic);
        assertEquals("C-001", clinic.getClinicId());
        assertEquals("District Six Clinic", clinic.getClinicName());
    }

    @Test
    void createClinic_withNullClinicId_returnsNull() {
        var clinic = ClinicFactory.createClinic(null, "District Six Clinic", "Cape Town", "0211234567");
        assertNull(clinic);
    }

    @Test
    void createClinic_withBlankClinicId_returnsNull() {
        var clinic = ClinicFactory.createClinic("   ", "District Six Clinic", "Cape Town", "0211234567");
        assertNull(clinic);
    }

    @Test
    void createClinic_withNullClinicName_returnsNull() {
        var clinic = ClinicFactory.createClinic("C-002", null, "Cape Town", "0211234567");
        assertNull(clinic);
    }

    @Test
    void createClinic_withNullLocationAndContact_stillReturnsClinic() {
        // location and contactNumber are optional
        var clinic = ClinicFactory.createClinic("C-003", "Bellville Clinic", null, null);
        assertNotNull(clinic);
    }
}