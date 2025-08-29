package com.clinic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "prescriptions")
public class Prescriptions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long prescription_id;

	@OneToOne
	@JoinColumn(name = "appointment_id", referencedColumnName = "appointment_id")
	private Appointments appointment;

	@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id")
	private Doctors doctor;

	@ManyToOne
	@JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
	private Patients patient;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String details;
}
