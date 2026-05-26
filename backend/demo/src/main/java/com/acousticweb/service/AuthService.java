package com.acousticweb.service;

import com.acousticweb.dto.UsuarioResponse;
import com.acousticweb.dto.LoginRequest;
import com.acousticweb.dto.RegisterRequest;
import com.acousticweb.entity.AppUser;
import com.acousticweb.enums.RolUsuario;
import com.acousticweb.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioResponse register(RegisterRequest request) {
        String correoNormalizado = request.correo().trim().toLowerCase();

        if (usuarioRepository.existsByCorreo(correoNormalizado)) {
            throw new IllegalArgumentException("Ya existe un usuario registrado con ese correo.");
        }

        AppUser usuario = AppUser.builder()
                .nombre(request.nombre().trim())
                .correo(correoNormalizado)
                .passwordHash(passwordEncoder.encode(request.password()))
                .rol(RolUsuario.USUARIO)
                .build();

        AppUser guardado = usuarioRepository.save(usuario);

        return new UsuarioResponse(
                guardado.getId(),
                guardado.getNombre(),
                guardado.getCorreo(),
                guardado.getRol().name()
        );
    }

    public UsuarioResponse login(LoginRequest request) {
        String correoNormalizado = request.correo().trim().toLowerCase();

        AppUser usuario = usuarioRepository.findByCorreo(correoNormalizado)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas."));

        if (!passwordEncoder.matches(request.password(), usuario.getPasswordHash())) {
            throw new IllegalArgumentException("Credenciales inválidas.");
        }

        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().name()
        );
    }
}