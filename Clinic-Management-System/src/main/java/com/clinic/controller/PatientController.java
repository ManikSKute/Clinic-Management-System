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
@RequestMapping("/patient")
@RequiredArgsConstructor
@PreAuthorize("hasRole('PATIENT')") 
public class PatientController {

    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;

    // Book appointment
    @PostMapping("/appointment/book/{doctorId}")
    public Appointments bookAppointment(@PathVariable Long doctorId, @RequestParam Long patientId) {
        return appointmentService.bookAppointment(patientId, doctorId);
    }

    // View prescriptions
    @GetMapping("/prescriptions/{patientId}")
    public List<Prescriptions> getPrescriptions(@PathVariable Long patientId) {
        return prescriptionService.getByPatientId(patientId);
    }
}
