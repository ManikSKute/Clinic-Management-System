package com.clinic.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.entity.Patients;

@Repository
public interface PatientRepo extends JpaRepository<Patients, Long> {
}
