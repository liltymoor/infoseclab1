package lilty.infra.infoseclab1.io.controller;

import lilty.infra.infoseclab1.core.entity.LabUser;
import lilty.infra.infoseclab1.core.service.UserService;
import lilty.infra.infoseclab1.io.dto.auth.LogPassDto;
import lilty.infra.infoseclab1.io.dto.auth.LoginResponse;
import lilty.infra.infoseclab1.io.dto.auth.RegisterResponse;
import lilty.infra.infoseclab1.utility.JwtUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtilityService jwtUtilityService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LogPassDto logPassDto) {
        requireCredentials(logPassDto);
        String username = logPassDto.getLogin().trim();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, logPassDto.getPass()));
        } catch (AuthenticationException ex) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "invalid credentials");
        }
        UserDetails userDetails = userService.loadUserByUsername(username);
        return ResponseEntity.ok(new LoginResponse(jwtUtilityService.generateToken(userDetails)));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody LogPassDto logPassDto) {
        requireCredentials(logPassDto);
        String username = logPassDto.getLogin().trim().toLowerCase();
        if (userService.checkIfUserExists(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username taken");
        }

        LabUser user = LabUser.builder()
                .username(username)
                .passwordHash(passwordEncoder.encode(logPassDto.getPass()))
                .role("USER")
                .build();

        userService.registerUser(user);
        UserDetails userDetails = userService.loadUserByUsername(username);
        return ResponseEntity.ok(new RegisterResponse(jwtUtilityService.generateToken(userDetails)));
    }

    private void requireCredentials(LogPassDto logPassDto) {
        if (logPassDto == null
                || logPassDto.getLogin() == null
                || logPassDto.getLogin().isBlank()
                || logPassDto.getPass() == null
                || logPassDto.getPass().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "login and password are required");
        }
    }
}
