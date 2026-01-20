package com.denyyyys.fluentLift.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.denyyyys.fluentLift.model.common.dto.request.AuthRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.request.UserRegistrationRequestDto;
import com.denyyyys.fluentLift.model.postgres.dto.response.AppUserResponseDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;
import com.denyyyys.fluentLift.service.AppUserService;
import com.denyyyys.fluentLift.service.JwtService;
import org.springframework.security.access.AccessDeniedException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {
    private final AppUserService appUserService;
    private final AppUserRepository appUserRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserRegistrationRequestDto userRegistrationRequest) {
        AppUser user = appUserService.addUser(userRegistrationRequest);
        return jwtService.generateToken(user.getId(), user.getName(), user.getEmail());
    }

    // Removed the role checks here as they are already managed in SecurityConfig
    @PostMapping("/generateToken")
    public ResponseEntity<String> authenticateAndGetToken(@RequestBody AuthRequestDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                        authRequest.getPassword()));

        if (authentication.isAuthenticated()) {
            String email = authRequest.getEmail();
            AppUser user = appUserRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User was not found with provided email"));

            String token = jwtService.generateToken(user.getId(), user.getName(), email);
            return ResponseEntity.ok(token);

        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("/userInfo")
    public ResponseEntity<AppUserResponseDto> getUserInfo(@AuthenticationPrincipal UserDetails user) {
        if (user == null) {
            throw new AccessDeniedException("User must be authenticated!");
        }

        return ResponseEntity.ok(appUserService.getUserInfo(user.getUsername()));
    }
}
