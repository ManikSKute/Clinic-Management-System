package com.clinic.controller;

import com.clinic.dto.AppointmentRequest;
import com.clinic.entity.Appointments;
import com.clinic.entity.Doctors;
import com.clinic.entity.Patients;
import com.clinic.entity.Prescriptions;
import com.clinic.service.AppointmentService;
import com.clinic.service.DoctorService;
import com.clinic.service.PrescriptionService;
import com.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_PATIENT')")
public class PatientController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final PatientService patientService;

    @GetMapping("/me")
    public ResponseEntity<Long> getPatientId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patient = patientService.findByUsername(username);
        if (patient != null) {
            return ResponseEntity.ok(patient.getPatient_id());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<Doctors>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }

    @PostMapping("/appointment/book/{doctorId}/{patientId}")
    public ResponseEntity<Appointments> bookAppointment(
            @PathVariable Long doctorId,
            @PathVariable Long patientId,
            @RequestBody AppointmentRequest request) {
        try {
            Appointments appointment = appointmentService.bookAppointment(
                    doctorId,
                    patientId,
                    request.getAppointmentDate(),
                    request.getAppointmentTime()
            );
            return ResponseEntity.ok(appointment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    
    @GetMapping("/appointment/{patientId}")
    public List<Appointments> viewAppointments(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsByPatient(patientId);
    }

    @GetMapping("/prescriptions/{patientId}")
    public List<Prescriptions> getPrescriptions(@PathVariable Long patientId) {
        return prescriptionService.getByPatientId(patientId);
    }
}