package com.educationalSystem.service;

import com.educationalSystem.mapper.UserMapper;
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
    private final UserMapper userMapper;

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(u -> userMapper.mapToDTO(u, new UserDTO()))
                .toList();
    }

    public UserDTO getUserById(Long id) {
        return userMapper.mapToDTO(findOrThrow(id), new UserDTO());
    }

    public UserDTO getUserByUsername(String username) {       // ← NEW
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + username));
        return userMapper.mapToDTO(user, new UserDTO());
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.delete(findOrThrow(id));
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

    private User findOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
    }
}