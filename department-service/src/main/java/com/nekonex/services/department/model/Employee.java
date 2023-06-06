package com.nekonex.services.department.model;

public record Employee(Long id, String name, int age, String position) {
	public Employee(String name, int age, String position) {
		this(null, name, age, position);
	}

	@Override
	public String toString() {
		return "Employee{" +
				"id=" + id +
				", name='" + name + '\'' +
				", age=" + age +
				", position='" + position + '\'' +
				'}';
	}
}
