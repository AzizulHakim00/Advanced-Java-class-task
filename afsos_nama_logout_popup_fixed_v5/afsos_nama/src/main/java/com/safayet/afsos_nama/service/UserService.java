package com.safayet.afsos_nama.service;

import com.safayet.afsos_nama.dto.ProfileDTO;
import com.safayet.afsos_nama.dto.RegistrationDTO;
import com.safayet.afsos_nama.model.User;
import com.safayet.afsos_nama.model.enums.Role;
import com.safayet.afsos_nama.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void register(RegistrationDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("An account already exists with this email");
        }

        User user = new User();
        BeanUtils.copyProperties(dto, user, "password", "confirmPassword");
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(Role.STUDENT);
        user.setEnabled(true);

        userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase()).orElse(null);
    }

    public ProfileDTO getProfile(User user) {
        ProfileDTO dto = new ProfileDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    public void updateProfile(User user, ProfileDTO dto) {
        BeanUtils.copyProperties(dto, user, "email");
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public void toggleUser(Integer id) {
        User user = userRepository.findById(id).orElse(null);

        if (user != null && user.getRole() != Role.ADMIN) {
            user.setEnabled(!user.isEnabled());
            userRepository.save(user);
        }
    }

    public long countUsers() {
        return userRepository.count();
    }

    public void createDefaultAdmin() {
        String email = "admin@afsosnama.com";

        if (userRepository.existsByEmail(email)) {
            return;
        }

        User admin = new User();
        admin.setName("Afsos Nama Admin");
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode("Admin123!"));
        admin.setUniversity("Afsos Nama");
        admin.setDepartment("Administration");
        admin.setCurrentSemester("Admin");
        admin.setRole(Role.ADMIN);
        admin.setEnabled(true);

        userRepository.save(admin);
    }
}
