package com.example.vehicledecoder;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Entity
public class Vehicle {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Vin cannot be blank")
	@Pattern(regexp = "^[A-HJ-NPR-Z0-9]{17}$", message = "VIN must be 17 characters: digits and capital letters, without I, O or Q")
	@Column(nullable = false, unique = true, length = 17)
	private String vin;

	private String make;

	private String model;

	@Column(name = "model_year", nullable = false)
	private int modelYear;

	public Vehicle() {

	}

	public Vehicle(Long id, String vin, String make, String model, int modelYear) {
		super();
		this.id = id;
		this.vin = vin;
		this.make = make;
		this.model = model;
		this.modelYear = modelYear;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVin() {
		return vin;
	}

	public void setVin(String vin) {
		this.vin = vin;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getModelYear() {
		return modelYear;
	}

	public void setModelYear(int modelYear) {
		this.modelYear = modelYear;
	}
}