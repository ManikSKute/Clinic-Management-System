package com.clinic.controller;

import com.clinic.entity.Doctors;
import com.clinic.entity.Patients;
import com.clinic.entity.Users;
import com.clinic.service.DoctorService;
import com.clinic.service.PatientService;
import com.clinic.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminController {

    private final DoctorService doctorService;
    private final PatientService patientService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/doctors")
    public ResponseEntity<Doctors> addDoctor(@RequestBody Doctors doctor) {
        Users user = doctor.getUser();
        Doctors savedDoctor = doctorService.registerDoctor(user, doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDoctor);
    }
    
    @GetMapping("/doctors")
    public ResponseEntity<List<Doctors>> getAllDoctors() {
        return ResponseEntity.ok(doctorService.getAllDoctors());
    }
    
    @DeleteMapping("/doctor/{id}")
    public void deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
    
    @PostMapping("/receptionists")
    public ResponseEntity<Users> addReceptionist(@RequestBody Users user) {
    	user.setPassword(passwordEncoder.encode(user.getPassword()));
    	return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }

    @GetMapping("/patients")
    public ResponseEntity<List<Patients>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
    @DeleteMapping("/patient/{id}")
    public void deletePatient(@PathVariable Long id) {
        patientService.deletePatient(id);
    }
}