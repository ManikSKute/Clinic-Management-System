package com.clinic.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "doctors")
public class Doctors {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long doctor_id;
	
	@OneToOne
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private Users user;
	
	@Column(nullable = false, length = 100)
	private String name;
	
	private String specialization;
	
	private String contact;
	
	private String availability;
}
