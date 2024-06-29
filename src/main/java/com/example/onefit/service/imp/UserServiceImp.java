package com.example.onefit.service.imp;

import com.example.onefit.dto.user.LoginDto;
import com.example.onefit.dto.user.RegisterDto;
import com.example.onefit.dto.user.UserDto;
import com.example.onefit.dto.web.JwtResponse;
import com.example.onefit.entity.Role;
import com.example.onefit.entity.UserEntity;
import com.example.onefit.exceptions.UserAlreadyExistsException;
import com.example.onefit.exceptions.UserNotFoundException;
import com.example.onefit.provider.JwtProvider;
import com.example.onefit.repository.RoleRepository;
import com.example.onefit.repository.UserRepository;
import com.example.onefit.service.UserService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserServiceImp implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RoleRepository roleRepository;

    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
        this.roleRepository = roleRepository;
    }

    @Override
    public JwtResponse login(LoginDto dto) {
        final UserEntity user = userRepository.findByEmail(dto.email())
                .orElseThrow(
                        () -> new UserNotFoundException("User not found by email: " + dto.email())
                );
        if (!passwordEncoder.matches(dto.password(), user.getPassword())) {
            throw new BadCredentialsException("Bad credentials");
        }
        return new JwtResponse(jwtProvider.generateToken(user), jwtProvider.getExpiration());
    }

    @Override
    public void register(RegisterDto dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("User already exists by email: " + dto.email());
        }

        UserEntity user = UserEntity.builder()
                .email(dto.email())
                .username(dto.username())
                .password(passwordEncoder.encode(dto.password()))
                .build();
        Role roles = roleRepository.findByName("USER").get();
        user.setRoles(Collections.singletonList(roles));
        userRepository.save(user);
    }

    @Override
    public boolean delete(Long id) {
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("Not found with id: " + id)
        );
        if(user==null){
            return false;
        }
        userRepository.delete(user);
        return true;
    }

    @Override
    public boolean update(UserDto dto) {
        UserEntity user = userRepository.findByEmail(dto.email()).orElseThrow(
                () -> new UserNotFoundException("Not found with email" + dto.email())
        );
        if(dto.email()!=null){
            user.setEmail(dto.email());
        }
        if(dto.username()!=null){
            user.setUsername(dto.username());
        }
        if(dto.name()!=null){
            user.setName(dto.name());
        }
        if(dto.bio()!=null){
            user.setBio(dto.bio());
        }
        userRepository.save(user);
        return true;
    }
}
