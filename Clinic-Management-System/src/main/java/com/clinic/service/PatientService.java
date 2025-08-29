package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.entity.Patients;
import com.clinic.entity.Users;
import com.clinic.repo.PatientRepo;
import com.clinic.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientService {

	private final PatientRepo patientRepo;
	private final UserRepo userRepo;
	
	public Patients registerPatient(Users user, Patients patient) {
		Users savedUser = userRepo.save(user);
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
}
