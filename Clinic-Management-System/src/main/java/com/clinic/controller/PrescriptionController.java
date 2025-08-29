package com.clinic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.entity.Prescriptions;
import com.clinic.service.PrescriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/prescription")
@RequiredArgsConstructor
public class PrescriptionController {

	private final PrescriptionService prescriptionService ;
	
	// 1. Add Prescription
	@PostMapping("/add")
	public Prescriptions addPrescription(@RequestBody Prescriptions prescription) {
		
		Long appointment_id = prescription.getAppointment().getAppointment_id();
		Long patient_id = prescription.getPatient().getPatient_id();
		Long doctor_id = prescription.getDoctor().getDoctor_id();
		String details = prescription.getDetails();
		
		return prescriptionService.addPrescription(appointment_id, patient_id, doctor_id, details);
	}
	
	// 2. Get Prescription By Patient Id
	@GetMapping("/{patient_id}")
	public List<Prescriptions> getByPatient(@PathVariable Long patient_id) {
		
		return prescriptionService.getByPatientId(patient_id);
	}
}
