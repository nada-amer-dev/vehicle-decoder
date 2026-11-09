package com.example.vehicledecoder;

import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public class VehicleService {

	private final VehicleRepository vehicleRepository;

	private final NhtsaClient nhtsaClient;

	public VehicleService(VehicleRepository vehicleRepository, NhtsaClient nhtsaClient) {
		this.vehicleRepository = vehicleRepository;
		this.nhtsaClient = nhtsaClient;
	}

	public Vehicle getVehicleByVin(String vin)

	{

		Optional<Vehicle> existing = vehicleRepository.findByVin(vin);

		if (existing.isPresent()) {

			return existing.get();
		}

		NhtsaResult result = nhtsaClient.decode(vin);

		if (result == null || result.make() == null || result.make().isBlank() || result.model() == null
				|| result.model().isBlank() || result.modelYear() == null || result.modelYear().isBlank()) {
			throw new VehicleNotFoundException("Could not decode VIN: " + vin);
		}
		Vehicle vehicle = new Vehicle();
		vehicle.setVin(vin);
		vehicle.setMake(result.make());
		vehicle.setModel(result.model());
		vehicle.setModelYear(Integer.parseInt(result.modelYear()));
		return vehicleRepository.save(vehicle);

	}

	public Vehicle saveVehicle(Vehicle vehicle) {
		String vin = vehicle.getVin();
		if (vehicleRepository.findByVin(vin).isPresent()) {
			throw new VehicleAlreadyExistsException("VIN already exists: " + vin);
		}
		return vehicleRepository.save(vehicle);
	}

}
