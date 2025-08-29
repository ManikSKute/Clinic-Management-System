package com.clinic.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.entity.Appointments;
import com.clinic.entity.Doctors;
import com.clinic.entity.Patients;

@Repository
public interface AppointmentRepo extends JpaRepository<Appointments, Long> {

	// Find all appointments for a patient
    List<Appointments> findByPatient(Patients patient);

    // Find all appointments for a doctor
    List<Appointments> findByDoctor(Doctors doctor);
}
