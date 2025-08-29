package com.clinic.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.clinic.entity.Appointments;
import com.clinic.entity.Status;
import com.clinic.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentController {

	private final AppointmentService appointmentService;
	
	// 1. Book appointment
	@GetMapping("/book/{patient_id}/{doctor_id}")
	public Appointments bookAppointment(@PathVariable Long patient_id, @PathVariable Long doctor_id) {
		return appointmentService.bookAppointment(patient_id, doctor_id);
	}
	
	// 2. Get appointment by ID
    @GetMapping("/{id}")
    public Appointments getOne(@PathVariable Long id) {
        return appointmentService.getAppointmentById(id);
    }
	
	// 3. Get all appointments
	@GetMapping
	public List<Appointments> getAll(){
		return appointmentService.getAllAppointments();
	}
	
	// 4. Get appointments by patient
    @GetMapping("/patient/{patientId}")
    public List<Appointments> getByPatient(@PathVariable Long patientId) {
        return appointmentService.getAppointmentsByPatient(patientId);
    }

    // 5. Get appointments by doctor
    @GetMapping("/doctor/{doctorId}")
    public List<Appointments> getByDoctor(@PathVariable Long doctorId) {
        return appointmentService.getAppointmentsByDoctor(doctorId);
    }
	
	// 6. Update appointment status
    @PutMapping("/{id}/status")
    public Appointments updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return appointmentService.updateAppointmentStatus(id, status);
    }
}
