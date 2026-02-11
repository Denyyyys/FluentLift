package com.denyyyys.fluentLift.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.denyyyys.fluentLift.exceptions.UserAlreadyExistsException;
import com.denyyyys.fluentLift.model.postgres.dto.request.UserRegistrationRequestDto;
import com.denyyyys.fluentLift.model.postgres.entity.AppUser;
import com.denyyyys.fluentLift.repo.postgres.AppUserRepository;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
    @Mock
    private AppUserRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @InjectMocks
    private AppUserService service;

    @Test
    void addUser_shouldEncodePasswordAndSaveUser() {
        String name = "Denys";
        String email = "denys@mail.com";
        String password = "plainPassword";
        String hashedPassword = "hashedPassword";
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(name, email, password);

        when(repository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        when(encoder.encode(dto.getPassword())).thenReturn(hashedPassword);

        when(repository.save(any(AppUser.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        AppUser savedUser = service.addUser(dto);

        assertNotNull(savedUser);
        assertEquals(email, savedUser.getEmail());
        assertEquals(hashedPassword, savedUser.getPassword());

        verify(repository).findByEmail(dto.getEmail());
        verify(encoder).encode(password);
        verify(repository).save(any(AppUser.class));
    }

    @Test
    void addUser_shouldThrowException_whenEmailExists() {
        UserRegistrationRequestDto dto = new UserRegistrationRequestDto(
                "John",
                "john@test.com",
                "password"
        );

        when(repository.findByEmail(dto.getEmail()))
                .thenReturn(Optional.of(new AppUser()));

        assertThrows(UserAlreadyExistsException.class,
                () -> service.addUser(dto));

        verify(repository, never()).save(any());
        verify(encoder, never()).encode(any());
    }

}
