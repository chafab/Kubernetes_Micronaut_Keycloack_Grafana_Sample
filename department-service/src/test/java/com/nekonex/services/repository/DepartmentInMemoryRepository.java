package com.nekonex.services.repository;

import com.nekonex.services.department.model.Department;
import com.nekonex.services.department.repository.IDepartmentRepository;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.context.scope.Refreshable;
import jakarta.inject.Inject;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//The following class is only used for tests
@Refreshable
@Requires(property = "in-memory-store.enabled", value = "true", defaultValue = "false")
public class DepartmentInMemoryRepository implements IDepartmentRepository {

    @Inject
    private DepartmentsInitialList departmentInitialList;

    private List<Department> departments = new ArrayList<>();

    @PostConstruct
    public void init() {
        departmentInitialList.getDepartments().forEach(department -> departments.add(department));
    }

    @Override
    public Department add(Department department) {
        long newId = departments.size() + 1;
        Department newDepartment = new Department(newId, department.organizationId(), department.name(), department.employees());
        departments.add(newDepartment);
        return newDepartment;
    }

    @Override
    public Department findById(Long id) {
        return departments.stream()
                .filter(department ->
                        department.id().equals(id))
                .findAny()
                .orElse(null);
    }

    @Override
    public List<Department> findAll() {
        return departments;
    }

    @Override
    public List<Department> findByOrganization(Long organizationId) {
        return departments.stream()
                .filter(department -> department.organizationId().equals(organizationId))
                .collect(Collectors.toList());
    }
}