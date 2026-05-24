package com.acousticweb.service;

import com.acousticweb.dto.AuthResponse;
import com.acousticweb.dto.LoginRequest;
import com.acousticweb.dto.RegisterRequest;
import com.acousticweb.entity.AppUser;
import com.acousticweb.enums.RolUsuario;
import com.acousticweb.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        String correoNormalizado = request.correo().trim().toLowerCase();

        if (userRepository.existsByCorreo(correoNormalizado)) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese correo.");
        }

        AppUser user = AppUser.builder()
                .nombre(request.nombre().trim())
                .correo(correoNormalizado)
                .passwordHash(passwordEncoder.encode(request.password()))
                .rol(RolUsuario.USUARIO)
                .build();

        AppUser saved = userRepository.save(user);

        return new AuthResponse(
                saved.getId(),
                saved.getNombre(),
                saved.getCorreo(),
                saved.getRol().name()
        );
    }

    public AuthResponse login(LoginRequest request) {
        String correoNormalizado = request.correo().trim().toLowerCase();

        AppUser user = userRepository.findByCorreo(correoNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        return new AuthResponse(
                user.getId(),
                user.getNombre(),
                user.getCorreo(),
                user.getRol().name()
        );
    }
}