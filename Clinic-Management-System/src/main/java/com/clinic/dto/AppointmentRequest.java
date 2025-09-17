package com.clinic.dto;

import lombok.Data;

@Data
public class AppointmentRequest {
    private String appointmentDate;
    private String appointmentTime;
}

