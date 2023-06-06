package com.nekonex.services.department.repository;

import com.nekonex.services.department.model.Department;

import java.util.List;

public interface IDepartmentRepository {

    Department add(Department department) ;
    Department findById(Long id);
    List<Department> findAll();
    List<Department> findByOrganization(Long organizationId);
}
