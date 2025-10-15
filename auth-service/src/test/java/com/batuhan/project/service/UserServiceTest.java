package com.batuhan.project.service;

import com.batuhan.project.entity.UserEntity;
import com.batuhan.project.exception.EmailAlreadyExistsException;
import com.batuhan.project.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSaveUserWhenEmailIsNotPresent() {
        String rawPassword = "password123";
        String encodedPassword = "encodedHash";

        when(userRepository.findByEmail("test@mail.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        userService.saveNewUser("test@mail.com", rawPassword);
        verify(userRepository, times(1)).save(argThat(user ->
                user.getPassword().equals(encodedPassword) &&
                        user.getEmail().equals("test@mail.com")
        ));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsAlreadyPresent() {
        when(userRepository.findByEmail("existing@mail.com")).thenReturn(Optional.of(new UserEntity()));
        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.saveNewUser("existing@mail.com", "password123");
        });
        verify(userRepository, never()).save(any());
    }
}