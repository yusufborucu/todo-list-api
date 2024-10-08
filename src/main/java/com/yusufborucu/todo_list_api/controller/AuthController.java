package com.yusufborucu.todo_list_api.controller;

import com.yusufborucu.todo_list_api.dto.AuthenticationResponse;
import com.yusufborucu.todo_list_api.dto.LoginRequest;
import com.yusufborucu.todo_list_api.dto.RegisterRequest;
import com.yusufborucu.todo_list_api.dto.RegisterResponse;
import com.yusufborucu.todo_list_api.model.User;
import com.yusufborucu.todo_list_api.security.JwtUtil;
import com.yusufborucu.todo_list_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    @Operation(
        summary = "Register API",
        description = "Register a new user",
        responses = {
            @ApiResponse(
                responseCode = "201",
                description = "Created",
                content = @Content(schema = @Schema(implementation = RegisterResponse.class))
            )
        }
    )
    public ResponseEntity<RegisterResponse> register(
        @Valid
        @Parameter(in = ParameterIn.DEFAULT, required = true)
        @RequestBody RegisterRequest registerRequest
    ) {
        User createdUser = userService.register(registerRequest);

        RegisterResponse registerResponse = new RegisterResponse();
        registerResponse.setId(createdUser.getId());
        registerResponse.setName(createdUser.getName());
        registerResponse.setEmail(createdUser.getEmail());

        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(
        summary = "Login API",
        description = "Login and get token",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(implementation = AuthenticationResponse.class))
            )
        }
    )
    public AuthenticationResponse login(
        @Valid
        @Parameter(in = ParameterIn.DEFAULT, required = true)
        @RequestBody LoginRequest loginRequest
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );
        final UserDetails userDetails = userService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthenticationResponse(token);
    }

    @Operation(
        summary = "Profile API",
        description = "Get profile infos",
        responses = {
            @ApiResponse(
                responseCode = "200",
                description = "OK",
                content = @Content(schema = @Schema(type = "string"))
            )
        }
    )
    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUser = authentication.getName();

        return ResponseEntity.ok(currentUser);
    }
}
