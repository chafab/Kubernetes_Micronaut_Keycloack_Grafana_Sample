package com.nekonex.services.employee.repository;

import java.util.ArrayList;
import java.util.List;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.runtime.context.scope.Refreshable;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.nekonex.services.employee.model.Employee;


@Refreshable
@Requires(missingProperty = "in-memory-store.enabled")
public class EmployeeMongoRepository implements IEmployeeRepository {


	private MongoClient mongoClient;

	@Property(name = "mongodb.database")
	private String mongodbDatabase;
	@Property(name = "mongodb.collection")
	private String mongodbCollection;

	EmployeeMongoRepository(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public Employee add(Employee employee) {
		Long newId = repository().countDocuments() + 1;
		Employee newEmployee = new Employee(newId, employee.organizationId(), employee.departmentId(),
				employee.name(), employee.age(), employee.position());
		repository().insertOne(newEmployee);
		return newEmployee;
	}

	public Employee findById(Long id) {
		return repository().find(Filters.eq("_id", id)).first();
	}

	public List<Employee> findAll() {
		final List<Employee> employees = new ArrayList<>();
		repository()
				.find()
				.iterator()
				.forEachRemaining(employees::add);
		return employees;
	}

	public List<Employee> findByDepartment(Long departmentId) {
		final List<Employee> employees = new ArrayList<>();
		repository()
				.find(Filters.eq("departmentId", departmentId))
				.iterator()
				.forEachRemaining(employees::add);
		return employees;
	}

	public List<Employee> findByOrganization(Long organizationId) {
		final List<Employee> employees = new ArrayList<>();
		repository()
				.find(Filters.eq("organizationId", organizationId))
				.iterator()
				.forEachRemaining(employees::add);
		return employees;
	}

	private MongoCollection<Employee> repository() {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.register("com.nekonex.services.employee.model")
				.build();

		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(pojoCodecProvider));
		return mongoClient.getDatabase(mongodbDatabase)
				.withCodecRegistry(pojoCodecRegistry)
				.getCollection(mongodbCollection, Employee.class);
	}
	@Override
	public long count() {	return repository().countDocuments();	}

}
