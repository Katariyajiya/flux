package com.flux.service;

import com.flux.entity.Department;
import com.flux.entity.Role;
import com.flux.entity.User;
import com.flux.repository.DepartmentRepository;
import com.flux.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public User createUser(Long id,
                           Role role ,
                           String name,
                           String email,
                           String phone,
                           String fcmToken,
                           Long departmentId){
        if (userRepository.existsByEmail(email)){
            throw new RuntimeException("User with this email already exists");
        }
        Department department = departmentRepository.findById(departmentId).orElseThrow(()->new RuntimeException("Department not found"));
        User user = new User(id,role,name,email,phone,fcmToken,department);

        return userRepository.save(user);
    }

    public List<User> getUserByDepartment(Long departmentId){
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(()->new RuntimeException("Department not found"));
        return userRepository.findByDepartment(department);
    }
}