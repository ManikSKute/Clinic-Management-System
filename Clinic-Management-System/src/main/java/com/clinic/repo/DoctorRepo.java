package com.clinic.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.entity.Doctors;

@Repository
public interface DoctorRepo extends JpaRepository<Doctors, Long> {
}
