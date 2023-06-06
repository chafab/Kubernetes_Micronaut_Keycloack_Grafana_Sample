package com.nekonex.services.department.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.util.ArrayList;
import java.util.List;

public record Department(@BsonProperty("_id")  Long id, Long organizationId, String name, List<Employee> employees) {
	public Department(Long organizationId, String name) {
		this(null, organizationId, name, new ArrayList<>());
	}

	public Department setEmployees(List<Employee> employees) {
		return new Department(id, organizationId, name, employees);
	}

	public Department addEmployee(Employee employee) {
		List<Employee> updatedEmployees = new ArrayList<>(employees);
		updatedEmployees.add(employee);
		return new Department(id, organizationId, name, updatedEmployees);
	}

	public Department removeEmployee(Employee employee) {
		List<Employee> updatedEmployees = new ArrayList<>(employees);
		updatedEmployees.remove(employee);
		return new Department(id, organizationId, name, updatedEmployees);
	}
}
