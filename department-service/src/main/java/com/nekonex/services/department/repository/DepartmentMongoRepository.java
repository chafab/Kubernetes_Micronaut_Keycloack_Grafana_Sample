package com.nekonex.services.department.repository;


import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import com.nekonex.services.department.model.Department;

import java.util.ArrayList;
import java.util.List;
@Singleton
@Requires(missingProperty = "in-memory-store.enabled")
public class DepartmentMongoRepository implements IDepartmentRepository {

	@Property(name = "mongodb.database")
	private String mongodbDatabase;
	@Property(name = "mongodb.collection")
	private String mongodbCollection;

	private MongoClient mongoClient;

	DepartmentMongoRepository(MongoClient mongoClient) {
		this.mongoClient = mongoClient;
	}

	public Department add(Department department) {
		Long newId = repository().countDocuments() + 1;
		Department newDepartment = new Department(newId, department.organizationId(), department.name(), department.employees());
		repository().insertOne(newDepartment);
		return newDepartment;
	}

	public Department findById(Long id) {
		return repository().find(Filters.eq("_id", id)).first();
	}

	public List<Department> findAll() {
		final List<Department> departments = new ArrayList<>();
		repository()
				.find()
				.iterator()
				.forEachRemaining(departments::add);
		return departments;
	}

	public List<Department> findByOrganization(Long organizationId) {
		final List<Department> departments = new ArrayList<>();
		repository()
				.find(Filters.eq("organizationId", organizationId))
				.iterator()
				.forEachRemaining(departments::add);
		return departments;
	}

	private MongoCollection<Department> repository() {
		CodecProvider pojoCodecProvider = PojoCodecProvider.builder()
				.register("com.nekonex.services.department.model")
				.build();

		CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
				MongoClientSettings.getDefaultCodecRegistry(),
				CodecRegistries.fromProviders(pojoCodecProvider));
		return mongoClient.getDatabase(mongodbDatabase)
				.withCodecRegistry(pojoCodecRegistry)
				.getCollection(mongodbCollection, Department.class);
	}

}
