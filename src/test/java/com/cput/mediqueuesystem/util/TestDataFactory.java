package com.cput.mediqueuesystem.util;

import com.cput.mediqueuesystem.domain.Role;

public class TestDataFactory {

    public static Role createTestRole() {
        return new Role.Builder()
                .setRoleId(1L)
                .setRoleName("ADMIN")
                .setDescription("Test Admin Role")
                .build();
    }

    public static Role createTestRole(Long id, String name) {
        return new Role.Builder()
                .setRoleId(id)
                .setRoleName(name)
                .build();
    }
}
