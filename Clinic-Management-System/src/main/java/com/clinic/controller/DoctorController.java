package com.clinic.controller;

import com.clinic.entity.Appointments;
import com.clinic.entity.Prescriptions;
import com.clinic.service.AppointmentService;
import com.clinic.service.PrescriptionService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@PreAuthorize("hasRole('DOCTOR')")
public class DoctorController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;

    // View Appointments assigned to doctor
    @GetMapping("/appointments/{doctorId}")
    public List<Appointments> getAppointments(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId);
    }

    // Add Prescription
    @PostMapping("/prescription/add")
    public Prescriptions addPrescription(@RequestBody Prescriptions prescription) {
        Long appointment_id = prescription.getAppointment().getAppointment_id();
        Long patient_id = prescription.getPatient().getPatient_id();
        Long doctor_id = prescription.getDoctor().getDoctor_id();
        String details = prescription.getDetails();

        return prescriptionService.addPrescription(appointment_id, patient_id, doctor_id, details);
    }
}
