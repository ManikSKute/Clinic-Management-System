package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.entity.Appointments;
import com.clinic.entity.Doctors;
import com.clinic.entity.Patients;
import com.clinic.entity.Prescriptions;
import com.clinic.repo.AppointmentRepo;
import com.clinic.repo.DoctorRepo;
import com.clinic.repo.PatientRepo;
import com.clinic.repo.PrescriptionRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PrescriptionService {

	private final PrescriptionRepo prescriptionRepo;
	private final AppointmentRepo appointmentRepo;
	private final DoctorRepo doctorRepo;
	private final PatientRepo patientRepo;

	// 1. Add Prescription
	public Prescriptions addPrescription(Long appointment_id, Long patient_id, Long doctor_id, String details) {
		Appointments appointment = appointmentRepo.findById(appointment_id)
				.orElseThrow(() -> new RuntimeException("Appointment Not Found For Id : " + appointment_id));
		
		Patients patient = patientRepo.findById(patient_id)
				.orElseThrow(() -> new RuntimeException("Patient Not Found For Id : " + patient_id));

		Doctors doctor = doctorRepo.findById(doctor_id)
				.orElseThrow(() -> new RuntimeException("Doctor Not Found For Id : " + doctor_id));
		
		Prescriptions prescription = new Prescriptions();
		prescription.setAppointment(appointment);
		prescription.setPatient(patient);
		prescription.setDoctor(doctor);
		prescription.setDetails(details);
		
		return prescriptionRepo.save(prescription);
	}

	// 2. Get Prescription By Patient
	public List<Prescriptions> getByPatientId(Long patient_id) {

		Patients patient = patientRepo.findById(patient_id)
				.orElseThrow(()-> new RuntimeException("Patient Not Found For Id : " + patient_id));
		
		return prescriptionRepo.findByPatient(patient);
	}
}
