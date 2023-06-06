package com.nekonex.services.repository;

import com.nekonex.services.department.model.Department;
import io.micronaut.context.annotation.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("test")
public class DepartmentsInitialList {

    private List<Department> departments = new ArrayList<>();

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> employees) {
        this.departments = employees;
    }

}
