package com.nandini.fullstack_backend.controller;

import com.nandini.fullstack_backend.dto.LoginRequest;
import com.nandini.fullstack_backend.model.User;
import com.nandini.fullstack_backend.repository.UserRepository;
import com.nandini.fullstack_backend.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userRepository.findAll()
                .stream()
                .filter(u -> u.getUsername().equals(loginRequest.getUsername()))
                .findFirst();

        if (userOpt.isPresent() &&
                passwordEncoder.matches(loginRequest.getPassword(), userOpt.get().getPassword())) {
            return jwtUtil.generateToken(loginRequest.getUsername());
        }

        throw new RuntimeException("Invalid credentials");
    }
}