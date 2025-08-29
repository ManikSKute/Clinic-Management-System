package com.clinic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.entity.Patients;
import com.clinic.entity.Role;
import com.clinic.entity.Users;
import com.clinic.service.PatientService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/patient")
@RequiredArgsConstructor
public class PatientController {

	private final PatientService patientService;
	
	@PostMapping("/register")
	public Patients register(@RequestBody Patients patient) {
		
		Users user = new Users();
		user.setUsername(patient.getUser().getUsername());
		user.setPassword(patient.getUser().getPassword());
		user.setRole(Role.PATIENT);
		
		patient.setUser(user);
		
		return patientService.registerPatient(user, patient);
	}
	
	@GetMapping("/{id}")
	public Patients getById(@PathVariable Long id) {
		return patientService.getPatientById(id);
	}
	
	@GetMapping
	public List<Patients> getAll(){
		return patientService.getAllPatients();
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		patientService.deletePatient(id);
	}
}
