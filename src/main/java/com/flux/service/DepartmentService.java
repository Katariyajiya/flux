package com.flux.service;

import com.flux.entity.Department;
import com.flux.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService{

    DepartmentRepository deptRepository;

    public DepartmentService(DepartmentRepository departmentRepository){
        this.deptRepository=departmentRepository;
    }

    public Department createDepartment(String name){
        Department department = new Department(name);
        return  deptRepository.save(department);
    }
    public List<Department> getAllDepartment(){
        return deptRepository.findAll();
    }
}