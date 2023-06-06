package com.nekonex.services.employee.model;

import org.bson.codecs.pojo.annotations.BsonProperty;

public record Employee(@BsonProperty("_id") Long id, Long organizationId, Long departmentId, String name, int age, String position) {
	public Employee(Long organizationId, Long departmentId, String name, int age, String position) {
		this(null, organizationId, departmentId, name, age, position);
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", organizationId=" + organizationId + ", departmentId=" + departmentId
				+ ", name=" + name + ", position=" + position + "]";
	}
}