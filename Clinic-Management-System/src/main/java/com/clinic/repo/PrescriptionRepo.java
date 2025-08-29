package com.clinic.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.entity.Patients;
import com.clinic.entity.Prescriptions;

@Repository
public interface PrescriptionRepo extends JpaRepository<Prescriptions, Long> {

	List<Prescriptions> findByPatient(Patients patient);
}
