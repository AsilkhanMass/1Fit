package com.example.onefit.service.imp;

import com.example.onefit.dto.user.LoginDto;
import com.example.onefit.dto.user.RegisterDto;
import com.example.onefit.dto.user.UserDto;
import com.example.onefit.dto.web.JwtResponse;
import com.example.onefit.entity.UserEntity;
import com.example.onefit.exceptions.UserAlreadyExistsException;
import com.example.onefit.exceptions.UserNotFoundException;
import com.example.onefit.provider.JwtProvider;
import com.example.onefit.repository.UserRepository;
import com.example.onefit.service.UserService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class UserServiceImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoginSuccess() {
        String email = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encrypting the password

        LoginDto loginDto = new LoginDto("testuser", password, email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(password, user.getPassword())).thenReturn(true);
        when(jwtProvider.generateToken(user)).thenReturn("generated_jwt_token");
        when(jwtProvider.getExpiration()).thenReturn(3600L); // Mock expiration time in seconds

        JwtResponse result = userService.login(loginDto);

        assertEquals("generated_jwt_token", result.getTokenType());
        // Add assertions for expiration time comparison
    }

    @Test
    public void testLoginUserNotFound() {
        String email = "nonexistent@example.com";
        String password = "password";

        LoginDto loginDto = new LoginDto("testuser", password, email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(loginDto));
    }

    @Test
    public void testLoginBadCredentials() {
        String email = "test@example.com";
        String password = "password";
        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encrypting the password

        LoginDto loginDto = new LoginDto("testuser", "wrong_password", email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong_password", user.getPassword())).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> userService.login(loginDto));
    }

    @Test
    public void testRegisterNewUser() {
        RegisterDto registerDto = new RegisterDto(
                "johndoe",
                "john.doe@example.com",
                "password"
        );

        when(userRepository.existsByEmail(registerDto.email())).thenReturn(false);

        // Call the register method
        userService.register(registerDto);

        // Optionally, add assertions for user creation logic if any
    }

    @Test
    public void testRegisterUserAlreadyExists() {
        RegisterDto registerDto = new RegisterDto(
                "johndoe",
                "john.doe@example.com",
                "password"
        );

        when(userRepository.existsByEmail(registerDto.email())).thenReturn(true);

        // Call the register method and expect UserAlreadyExistsException
        assertThrows(UserAlreadyExistsException.class, () -> userService.register(registerDto));
    }

    @Test
    public void testDeleteExistingUser() {
        Long userId = 1L;
        UserEntity user = new UserEntity();
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        boolean result = userService.delete(userId);

        assertTrue(result);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteNonExistingUser() {
        Long userId = 2L;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));

        verify(userRepository, never()).delete(any());
    }

    @Test
    public void testUpdateExistingUser() {
        UserDto userDto = new UserDto(
                "John Doe",
                "johndoe",
                "+123456789",
                "Some bio",
                "Male",
                "john.doe@example.com",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        UserEntity existingUser = new UserEntity();
        existingUser.setId(1L);
        existingUser.setName("Old Name");
        existingUser.setUsername("oldusername");
        existingUser.setEmail("old.email@example.com");

        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.of(existingUser));

        boolean result = userService.update(userDto);

        assertTrue(result);
        assertEquals(userDto.name(), existingUser.getName());
        assertEquals(userDto.username(), existingUser.getUsername());
        assertEquals(userDto.email(), existingUser.getEmail());
        assertEquals(userDto.bio(), existingUser.getBio());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    public void testUpdateNonExistingUser() {
        UserDto userDto = new UserDto(
                "John Doe",
                "johndoe",
                "+123456789",
                "Some bio",
                "Male",
                "john.doe@example.com",
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userRepository.findByEmail(userDto.email())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.update(userDto));

        verify(userRepository, never()).save(any());
    }
}