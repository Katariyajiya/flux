package com.flux.controller;

import com.flux.dto.UserApprovalRequest;
import com.flux.dto.UserResponse;
import com.flux.entity.Department;
import com.flux.entity.Role;
import com.flux.entity.User;
import com.flux.entity.UserStatus;
import com.flux.repository.DepartmentRepository;
import com.flux.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public AdminUserController(
            UserRepository userRepository,
            DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @PatchMapping("/{userId}/approve")
    public ResponseEntity<String> approveUser(
            @PathVariable Long userId,
            @RequestBody UserApprovalRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found"));

        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalArgumentException(
                    "User is not pending approval");
        }

        Role role;
        try {
            role = Role.valueOf(request.getRole().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role");
        }

        Department department = departmentRepository
                .findById(request.getDepartmentId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Department not found"));

        user.setRole(role);
        user.setDepartment(department);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);

        return ResponseEntity.ok(
                "User approved successfully"
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<List<UserResponse>> getPendingUsers() {

        List<UserResponse> users = userRepository
                .findByStatus(UserStatus.PENDING)
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole() != null
                                ? user.getRole().name()
                                : null,
                        user.getDepartment() != null
                                ? user.getDepartment().getId()
                                : null,
                        user.getStatus().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
    @PatchMapping("/{userId}/disable")
    public ResponseEntity<String> disableUser(@PathVariable Long userId){
        User user = userRepository.findById(userId).orElseThrow(()-> new IllegalArgumentException("User not found"));
        user.setStatus(UserStatus.DISABLED);
        userRepository.save(user);

        return ResponseEntity.ok("User disabled succesfully");
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {

        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole() != null
                                ? user.getRole().name()
                                : null,
                        user.getDepartment() != null
                                ? user.getDepartment().getId()
                                : null,
                        user.getStatus().name()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(users);
    }
}