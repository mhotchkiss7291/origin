package com.studytrails.tutorials.springinjectionproperties;

import java.util.Properties;

public class Car {

	private String chassisNumber;
	private String name;
	private Properties specifications;

	public String getChassisNumber() {
		return chassisNumber;
	}

	public void setChassisNumber(String chassisNumber) {
		this.chassisNumber = chassisNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Properties getSpecifications() {
		return specifications;
	}

	public void setSpecifications(Properties specifications) {
		this.specifications = specifications;
	}

}