package com.clinic.repo;

import com.clinic.entity.Patients;
import com.clinic.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepo extends JpaRepository<Patients, Long> {
    Patients findByUser(Users user);
}