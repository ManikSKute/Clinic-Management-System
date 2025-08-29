package com.clinic.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.clinic.entity.Users;
import com.clinic.repo.UserRepo;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;

    public Users createUser(Users user) {
        return userRepo.save(user);
    }

    public Users getUserById(Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public List<Users> getAllUsers() {
        return userRepo.findAll();
    }

    public void deleteUser(Long id) {
        userRepo.deleteById(id);
    }
}
