package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.entity.Patients;
import com.clinic.entity.Users;
import com.clinic.repo.PatientRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepo patientRepo;
	private final UserService userService;
	
	public Patients registerPatient(Users user, Patients patient) {
		Users savedUser = userService.createUser(user);
		patient.setUser(savedUser);
		return patientRepo.save(patient);
	}
	
	public Patients getPatientById(Long id) {
		return patientRepo.findById(id).orElse(null);
	}
	
	public List<Patients> getAllPatients() {
		return patientRepo.findAll();
	}
	
	public void deletePatient(Long id) {
		patientRepo.deleteById(id);
	}

	public Patients findByUsername(String username) {
        Users user = userService.findByUsername(username).orElse(null);
        if (user == null) return null;
        return patientRepo.findByUser(user);
    }
}
