package com.nekonex.services.department.client;

import java.util.List;

import com.nekonex.services.department.model.Employee;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Header;
import io.micronaut.http.client.annotation.Client;

@Client(id = "employee", path = "/api/employees")
public interface EmployeeClient {

	@Get("/department/{departmentId}")
	List<Employee> findByDepartment(@Header("X-TRACE-ID") String traceId, Long departmentId);

}
