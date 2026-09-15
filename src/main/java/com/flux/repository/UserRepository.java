package com.flux.repository;

import com.flux.entity.Department;
import com.flux.entity.Role;
import com.flux.entity.User;
import com.flux.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findByDepartment(Department department);
    List<User> findByDepartmentAndRole(Department department, Role role);
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByStatus(UserStatus userStatus);
}
