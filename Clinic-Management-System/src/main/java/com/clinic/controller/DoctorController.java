package com.clinic.controller;

import com.clinic.dto.miniPrescription;
import com.clinic.entity.Appointments;
import com.clinic.entity.Doctors;
import com.clinic.entity.Prescriptions;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;
import com.clinic.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_DOCTOR')")
public class DoctorController {

    private final PrescriptionService prescriptionService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    
    @GetMapping("/me")
    public ResponseEntity<Long> getPatientId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Doctors doctor = doctorService.findByUsername(username);
        if (doctor != null) {
            return ResponseEntity.ok(doctor.getDoctor_id());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/appointments/{doctorId}")
    public List<Appointments> getAppointments(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId);
    }

    @PostMapping("/prescription/add")
    public Prescriptions addPrescription(@RequestBody miniPrescription prescription) {
        Long appointment_id = prescription.getAppointment_id();
        Long patient_id = prescription.getPatient_id();
        Long doctor_id = prescription.getDoctor_id();
        String details = prescription.getDetails();

        return prescriptionService.addPrescription(appointment_id, patient_id, doctor_id, details);
    }
}