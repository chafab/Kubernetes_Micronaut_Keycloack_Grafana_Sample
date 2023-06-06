package com.nekonex.services.department.controller;

import com.nekonex.services.department.client.EmployeeClient;
import com.nekonex.services.department.model.Department;
import com.nekonex.services.department.repository.IDepartmentRepository;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/departments")
@Slf4j
@OpenAPIDefinition(
		info = @Info(
				title = "Department Controller",
				version = "0.0",
				description = "Nekonex API",
				license = @License(name = "GPL3.0", url = "https://www.gnu.org/licenses/gpl-3.0.en.html")
		)
)
public class DepartmentController {
	private IDepartmentRepository repository;
	private EmployeeClient employeeClient;

	DepartmentController(IDepartmentRepository repository, EmployeeClient employeeClient) {
		this.repository = repository;
		this.employeeClient = employeeClient;
	}

	@Operation(summary = "Add a department")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Added Department")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Post
	public Department add(@Body @RequestBody(description = "Department") Department department) {
		log.info("Department add: {}", department);
		return repository.add(department);
	}

	@Operation(summary = "Find a department")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The department that was found")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/{id}")
	public Department findById(Long id) {
		log.info("Department find: id={}", id+1);
		return repository.findById(id);
		//return null; //test
	}

	@Operation(summary = "Get all departments")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All the departments in the system")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get
	public List<Department> findAll() {
		log.info("Department find");
		return repository.findAll();
	}

	@Operation(summary = "Find all departments by organization")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The list of departments matching the specified organization")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/organization/{organizationId}")
	public List<Department> findByOrganization(Long organizationId) {
		log.info("Department find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@Operation(summary = "Find all departments by organization")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "The list of departments matching the specified organization, this also reset the list of employees in the department by calling the employee service")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/organization/{organizationId}/with-employees")
	public List<Department> findByOrganizationWithEmployees(@RequestAttribute("traceId") String uuid, Long organizationId) {
		log.info("Department find: organizationId={}, uuid{}", organizationId, uuid);
		List<Department> departments = repository.findByOrganization(organizationId);
		departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(uuid, d.id())));
		return departments;
	}

	@Operation(summary = "Simple Hello endpoint for Authenticated Users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Hello authenticated user~")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/SecureHello")
	public String hello(Authentication auth) {
		log.info("Secure hello " + auth.getName());
		return "Hello authenticated user~";
	}

	@Operation(summary = "Simple Hello endpoint for Authenticated or Anonymous users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Anonymous hello")
	})
	@Secured(SecurityRule.IS_ANONYMOUS)
	@Get("/AnonymousHello")
	public String helloAnonymous() {
		log.info("Anonymous hello");
		return "Hello Anonymous";
	}
}
