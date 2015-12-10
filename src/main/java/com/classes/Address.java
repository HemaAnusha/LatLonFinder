package com.Pojo.classes;

public class Address {
	private String city;
	private String line;
	private String country;

	public Address(String city, String line, String country) {
		this.city = city;
		this.line = line;
		this.country = country;
	}

	public String toString() {
		return city + " " + line + " " + country;
	}
}
