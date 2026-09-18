package com.cput.mediqueuesystem.factory;

import com.cput.mediqueuesystem.domain.Role;
import com.cput.mediqueuesystem.util.Helper;

/*
 * RoleFactory.java
 * Validates input and builds Role objects. Returns null if
 * required fields are missing or invalid, so callers can check
 * for a null result before persisting.
 *
 * Author: Charmaine Dlamini
 * Date: 05 August 2026
 */

public class RoleFactory {

    public static Role createRole(String roleId, String roleName) {

        if (Helper.isNullOrEmpty(roleId) || Helper.isNullOrEmpty(roleName)) {
            return null;
        }

        // Parse roleId from String to Long
        Long id;
        try {
            id = Long.parseLong(roleId);
        } catch (NumberFormatException e) {
            return null; // Invalid roleId format
        }

        return new Role.Builder()
                .setRoleId(id)
                .setRoleName(roleName)
                .build();
    }

    // Overloaded method for creating Role without ID (for auto-generated IDs)
    public static Role createRole(String roleName) {

        if (Helper.isNullOrEmpty(roleName)) {
            return null;
        }

        return new Role.Builder()
                .setRoleName(roleName)
                .build();
    }
}
