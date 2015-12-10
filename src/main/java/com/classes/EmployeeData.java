package com.Pojo.classes;

public class EmployeeData {

	private String name;
	private int id;
	private Address address;

	public EmployeeData(String name, int id, Address address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	public void show() {
		System.out.println(id + " " + name + " " + address);
	}
}
