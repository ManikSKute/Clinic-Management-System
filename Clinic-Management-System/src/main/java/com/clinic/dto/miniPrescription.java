package com.clinic.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class miniPrescription {
    private Long appointment_id;
    private Long patient_id;
    private Long doctor_id;
    private String details;
}
