package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.entity.Doctors;
import com.clinic.entity.Users;
import com.clinic.repo.DoctorRepo;
import com.clinic.repo.UserRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorService {

	private final UserRepo userRepo;
	private final DoctorRepo doctorRepo;
	
	public Doctors registerDoctor(Users user, Doctors doctor) {
		Users savedUser = userRepo.save(user);
		doctor.setUser(savedUser);
		return doctorRepo.save(doctor);
	}
	
	public Doctors getDoctorById(Long id) {
		return doctorRepo.findById(id).orElse(null);
	}
	
	public List<Doctors> getAllDoctors(){
		return doctorRepo.findAll();
	}
	
	public void deleteDoctor(Long id) {
		doctorRepo.deleteById(id);
	}
}
