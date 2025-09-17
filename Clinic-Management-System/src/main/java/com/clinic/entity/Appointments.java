package com.clinic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "appointments")
public class Appointments {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long appointment_id;
	
	@ManyToOne
	@JoinColumn(name = "patient_id", referencedColumnName = "patient_id")
	private Patients patient;
	
	@ManyToOne
	@JoinColumn(name = "doctor_id", referencedColumnName = "doctor_id")
	private Doctors doctor;
	
	@Column(nullable = false, length = 20)
	private String appointment_date;
	
	@Column(nullable = false, length = 20)
	private String appointment_time;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Status status;
}
