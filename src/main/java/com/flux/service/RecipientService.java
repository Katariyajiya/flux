package com.flux.service;

import com.flux.entity.Department;
import com.flux.entity.User;
import com.flux.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecipientService {

    private final UserRepository userRepository;

    public RecipientService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findRecipients(Department department) {
        return userRepository.findByDepartment(department);
    }
}