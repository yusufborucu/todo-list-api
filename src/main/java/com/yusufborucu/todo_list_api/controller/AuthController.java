package com.yusufborucu.todo_list_api.controller;

import com.yusufborucu.todo_list_api.dto.AuthenticationResponse;
import com.yusufborucu.todo_list_api.dto.LoginRequest;
import com.yusufborucu.todo_list_api.dto.RegisterRequest;
import com.yusufborucu.todo_list_api.dto.RegisterResponse;
import com.yusufborucu.todo_list_api.model.User;
import com.yusufborucu.todo_list_api.security.JwtUtil;
import com.yusufborucu.todo_list_api.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthController(UserService userService, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        User createdUser = userService.register(registerRequest);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(createdUser.getId());
        registerResponse.setName(createdUser.getName());
        registerResponse.setEmail(createdUser.getEmail());

        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(token);
    }

    @GetMapping("/home")
    public String home() {
        return "Success";
    }
}
