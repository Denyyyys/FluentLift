package com.denyyyys.fluentLift.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.denyyyys.fluentLift.exceptions.UserAlreadyExistsException;
import com.denyyyys.fluentLift.model.postgres.dto.request.UserRegistrationRequestDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.model.postgres.mapper.AppUserMapper;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = repository.findByEmail(username);

        if (appUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }

        AppUser user = appUser.get();
        return new AppUserDetails(user);
    }

    public String addUser(UserRegistrationRequestDto userInfo) {
        repository.findByEmail(userInfo.getEmail()).ifPresent((appUser) -> {
            throw new UserAlreadyExistsException();
        });

        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        AppUser user = AppUserMapper.toEntity(userInfo);
        repository.save(user);
        return "User added successfully!";
    }

}
