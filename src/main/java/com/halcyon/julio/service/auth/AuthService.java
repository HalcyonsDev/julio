package com.halcyon.julio.service.auth;

import com.halcyon.julio.dto.auth.SignUpDto;
import com.halcyon.julio.model.Token;
import com.halcyon.julio.model.User;
import com.halcyon.julio.security.AuthRequest;
import com.halcyon.julio.security.AuthResponse;
import com.halcyon.julio.security.JwtAuthentication;
import com.halcyon.julio.service.token.TokenService;
import com.halcyon.julio.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final TokenService tokenService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse signup(SignUpDto dto) {
        if (userService.existsByEmail(dto.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this email already exists.");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .firstname(dto.getFirstname())
                .lastname(dto.getLastname())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();

        userService.create(user);

        String accessToken = jwtProvider.generateToken(user, false);
        String refreshToken = jwtProvider.generateToken(user, true);

        tokenService.create(new Token(user, refreshToken));

        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        String accessToken = jwtProvider.generateToken(user, false);
        String refreshToken = jwtProvider.generateToken(user, true);

        tokenService.create(new Token(user, refreshToken));

        return new AuthResponse(accessToken, refreshToken);
    }

    public String delete(AuthRequest authRequest) {
        User user = userService.findByEmail(authRequest.getEmail());

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        userService.delete(user);
        return "User deleted successfully.";
    }

    public AuthResponse getTokenByRefresh(String refreshToken, boolean isRefresh) {
        String subject = jwtProvider.extractRefreshClaims(refreshToken).getSubject();

        if (!jwtProvider.isValidRefreshToken(refreshToken)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Refresh token is not valid.");
        }

        Token token = tokenService.findByValue(refreshToken);

        if (!token.getValue().equals(refreshToken) || !token.getOwner().getEmail().equals(subject)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong data.");
        }

        User user = userService.findByEmail(subject);

        String accessToken = jwtProvider.generateToken(user, false);
        String newRefreshToken = null;

        if (isRefresh) {
            newRefreshToken = jwtProvider.generateToken(user, true);
            tokenService.create(new Token(user, newRefreshToken));
        }

        return new AuthResponse(accessToken, newRefreshToken);
    }

    public JwtAuthentication getAuthInfo() {
        return (JwtAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
}