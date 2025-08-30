package com.clinic.controller;

import com.clinic.entity.Appointments;
import com.clinic.entity.Status;
import com.clinic.service.AppointmentService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/receptionist")
@RequiredArgsConstructor
@PreAuthorize("hasRole('RECEPTIONIST')")
public class ReceptionistController {

    private final AppointmentService appointmentService;

    // View all appointments
    @GetMapping("/appointments")
    public List<Appointments> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // Update status of an appointment
    @PutMapping("/appointment/{id}/status")
    public Appointments updateStatus(@PathVariable Long id, @RequestParam Status status) {
        return appointmentService.updateAppointmentStatus(id, status);
    }
}
