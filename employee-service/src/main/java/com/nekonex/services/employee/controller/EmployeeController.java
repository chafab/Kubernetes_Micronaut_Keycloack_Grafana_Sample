package com.nekonex.services.employee.controller;

import com.nekonex.services.employee.model.Employee;
import com.nekonex.services.employee.repository.EmployeeRepository;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.rules.SecurityRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import java.util.List;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller("/api/employees")
@OpenAPIDefinition(
		info = @Info(
				title = "Employee Controller",
				version = "0.0",
				description = "Nekonex API",
				license = @License(name = "GPL3.0", url = "https://www.gnu.org/licenses/gpl-3.0.en.html")
		)
)
public class EmployeeController {

	private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

	@Inject
	EmployeeRepository repository;

	@Operation(summary = "Add an employee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Added Employee")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Post
	public Employee add(@Body @RequestBody(description = "Employee") Employee employee) {
		LOGGER.info("Employee add: {}", employee);
		return repository.add(employee);
	}

	@Operation(summary = "Find an employee")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Found Employee")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/{id}")
	public Employee findById(Long id) {
		LOGGER.info("Employee find: id={}", id);
		return repository.findById(id);
	}

	@Operation(summary = "Get all employees")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "All the employees")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get
	public List<Employee> findAll() {
		LOGGER.info("Employees find");
		return repository.findAll();
	}

	@Operation(summary = "Get all employees for a given department")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of employees")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/department/{departmentId}")
	public List<Employee> findByDepartment(Long departmentId) {
		LOGGER.info("Employees find: departmentId={}", departmentId);
		return repository.findByDepartment(departmentId);
	}

	@Operation(summary = "Get all employees for a given organization")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "List of employees")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/organization/{organizationId}")
	public List<Employee> findByOrganization(Long organizationId) {
		LOGGER.info("Employees find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@Operation(summary = "Get the number of employees handled by the services")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Number of employees")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/count")
	long count() {
		LOGGER.info("Count");
		return repository.count();
	}

	@Operation(summary = "Simple Hello endpoint for Authenticated Users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Hello authenticated user~")
	})
	@SecurityRequirement(name = "BearerAuth", scopes = {})
	@Get("/SecureHello")
	public String hello(Authentication auth) {
		LOGGER.info("Secure hello " + auth.getName());
		return "Hello authenticated user~";
	}

	@Operation(summary = "Simple Hello endpoint for Authenticated or Anonymous users")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Anonymous hello")
	})
	@Secured(SecurityRule.IS_ANONYMOUS)
	@Get("/AnonymousHello")
	public String helloAnonymous() {
		LOGGER.info("Anonymous hello");
		return "Hello Anonymous";
	}
}
