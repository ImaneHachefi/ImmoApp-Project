package main.immoapp.service.impl;

import main.immoapp.config.JwtConfig;
import main.immoapp.dto.request.*;
import main.immoapp.dto.response.AuthResponse;
import main.immoapp.entity.*;
import main.immoapp.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email déjà utilisé");
        }

        Role role = roleRepository.findByNom(request.getRole())
                .orElseThrow(() -> new RuntimeException("Rôle introuvable : " + request.getRole()));

        User user = User.builder()
                .nom(request.getNom())
                .email(request.getEmail())
                .motDePasse(passwordEncoder.encode(request.getMotDePasse()))
                .dateCreation(LocalDate.now())
                .role(role)
                .build();

        userRepository.save(user);

        String token = jwtConfig.generateToken(user.getEmail(), role.getNom());

        return AuthResponse.builder()
                .token(token)
                .nom(user.getNom())
                .email(user.getEmail())
                .role(role.getNom())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable"));

        String token = jwtConfig.generateToken(user.getEmail(), user.getRole().getNom());

        return AuthResponse.builder()
                .token(token)
                .nom(user.getNom())
                .email(user.getEmail())
                .role(user.getRole().getNom())
                .build();
    }
}