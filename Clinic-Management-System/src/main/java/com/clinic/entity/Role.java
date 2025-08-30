package com.clinic.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    DOCTOR,
    PATIENT,
    RECEPTIONIST;

    @Override
    public String getAuthority() {
        return "ROLE_" + name();
    }
}
