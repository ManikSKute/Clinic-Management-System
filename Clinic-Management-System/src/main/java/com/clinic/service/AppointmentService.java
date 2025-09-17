package com.clinic.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.clinic.entity.Appointments;
import com.clinic.entity.Doctors;
import com.clinic.entity.Patients;
import com.clinic.entity.Status;
import com.clinic.repo.AppointmentRepo;
import com.clinic.repo.DoctorRepo;
import com.clinic.repo.PatientRepo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppointmentService {

	private final AppointmentRepo appointmentRepo;
	private final PatientRepo patientRepo;
	private final DoctorRepo doctorRepo;
	
	// 1. Book appointment
	public Appointments bookAppointment(Long doctor_id, Long patient_id, String date, String time) {
	    
	    Patients patient = patientRepo.findById(patient_id)
	            .orElseThrow(() -> new RuntimeException("Patient Not Found For Id : " + patient_id));

	    Doctors doctor = doctorRepo.findById(doctor_id)
	            .orElseThrow(() -> new RuntimeException("Doctor Not Found For Id : " + doctor_id));

	    Appointments appointment = new Appointments();
	    appointment.setPatient(patient);
	    appointment.setDoctor(doctor);
	    appointment.setAppointment_date(date);
	    appointment.setAppointment_time(time);
	    appointment.setStatus(Status.PENDING);

	    return appointmentRepo.save(appointment);
	}

	
	// 2. Get appointment by ID
	public Appointments getAppointmentById(Long id) {
		return appointmentRepo.findById(id).orElse(null);
	}
	
	// 3. Get all appointments
	public List<Appointments> getAllAppointments(){
		return appointmentRepo.findAll();
	}
	
	// 4. Get appointment by patient
	public List<Appointments> getAppointmentsByPatient(Long patient_id) {
        Patients patient = patientRepo.findById(patient_id)
                .orElseThrow(() -> new RuntimeException("Patient Not Found For Id : " + patient_id));
        return appointmentRepo.findByPatient(patient);
    }

	// 5. Get appointment by Doctor
    public List<Appointments> getAppointmentsByDoctor(Long doctor_id) {
        Doctors doctor = doctorRepo.findById(doctor_id)
                .orElseThrow(() -> new RuntimeException("Doctor Not Found For Id : " + doctor_id));
        return appointmentRepo.findByDoctor(doctor);
    }
	
	// 6. Update appointment status
	public Appointments updateAppointmentStatus(Long appointment_id, Status status) {
		
		Appointments appointment = appointmentRepo.findById(appointment_id)
				.orElseThrow(()-> new RuntimeException("Appointment Not Found For Id : " + appointment_id));
		
		appointment.setStatus(status);
		return appointmentRepo.save(appointment);
	}
	
}
