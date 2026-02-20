package com.educationalSystem.service;

import com.educationalSystem.converter.UserConverter;
import com.educationalSystem.dto.UserDTO;
import com.educationalSystem.entity.user.User;
import com.educationalSystem.exception.ResourceNotFoundException;
import com.educationalSystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserManagementService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;          // ← injected, not inlined

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> userConverter.convertToDTO(u, new UserDTO()))
                .toList();
    }

    public UserDTO getUserById(Long id) {
        return userConverter.convertToDTO(findOrThrow(id), new UserDTO());
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found: " + id);
        }
        userRepository.deleteById(id);
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }

    @Transactional
    public void deactivateUser(Long id) {
        User user = findOrThrow(id);
        user.setActive(false);
        userRepository.save(user);
    }

    @Transactional
    public void activateUser(Long id) {
        User user = findOrThrow(id);
        user.setActive(true);
        userRepository.save(user);
    }
}