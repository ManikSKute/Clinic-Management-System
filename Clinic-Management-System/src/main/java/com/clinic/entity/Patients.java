package com.clinic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "patients")
public class Patients {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long patient_id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private Users user;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	private Integer age;
	
	@Enumerated(EnumType.STRING)
	private Gender gender;
	
	private String contact;
	
	private String address;
	
	@Column(columnDefinition = "TEXT")
	private String medical_history;
}
