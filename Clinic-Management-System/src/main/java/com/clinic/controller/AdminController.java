package com.clinic.controller;

import com.clinic.entity.Doctors;
import com.clinic.entity.Patients;
import com.clinic.service.DoctorService;
import com.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final DoctorService doctorService;
    private final PatientService patientService;

    // Manage Doctors
    @GetMapping("/doctors")
    public List<Doctors> getAllDoctors() {
        return doctorService.getAllDoctors();
    }

    @DeleteMapping("/doctor/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }

    // Manage Patients
    @GetMapping("/patients")
    public List<Patients> getAllPatients() {
        return patientService.getAllPatients();
    }

    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}
