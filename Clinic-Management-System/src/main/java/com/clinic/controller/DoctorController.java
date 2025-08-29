package com.clinic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.entity.Doctors;
import com.clinic.entity.Role;
import com.clinic.entity.Users;
import com.clinic.service.DoctorService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/doctor")
@RequiredArgsConstructor
public class DoctorController {

	private final DoctorService doctorService;
	
	// Register new doctor
    @PostMapping("/register")
    public Doctors register(@RequestBody Doctors doctor) {
        Users user = new Users();
        user.setUsername(doctor.getUser().getUsername());
        user.setPassword(doctor.getUser().getPassword());
        user.setRole(Role.DOCTOR);

        doctor.setUser(user);
        return doctorService.registerDoctor(user, doctor);
    }

    // Get doctor by ID
    @GetMapping("/{id}")
    public Doctors getOne(@PathVariable Long id) {
        return doctorService.getDoctorById(id);
    }

    // Get all doctors
    @GetMapping
    public List<Doctors> getAll() {
        return doctorService.getAllDoctors();
    }

    // Delete doctor
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
    }
}
