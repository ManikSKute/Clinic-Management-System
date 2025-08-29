package com.clinic.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.clinic.entity.Users;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
}
