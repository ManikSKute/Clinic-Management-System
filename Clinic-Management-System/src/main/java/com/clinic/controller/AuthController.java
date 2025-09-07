package com.clinic.controller;

import com.clinic.dto.AuthRequest;
import com.clinic.dto.AuthResponse;
import com.clinic.entity.Patients;
import com.clinic.entity.Users;
import com.clinic.security.JwtUtil;
import com.clinic.service.PatientService;
import com.clinic.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final PatientService patientService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/register/admin")
    public ResponseEntity<?> register(@RequestBody Users user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }
    
    @PostMapping("/register/patient")
    public ResponseEntity<?> register(@RequestBody Patients patient) {
        Users user = patient.getUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.registerPatient(user, patient));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            Users user = userService.findByUsername(request.getUsername()).get();

            String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());

            return ResponseEntity.ok(new AuthResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(401).body("Invalid Credentials");
        }
    }
}