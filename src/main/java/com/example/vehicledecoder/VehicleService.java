package com.example.vehicledecoder;

import org.springframework.stereotype.Service;

@Service
public class VehicleService {

	private final VehicleRepository vehicleRepository;

	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository = vehicleRepository;
	}

	public Vehicle getVehicleByVin(String vin)

	{

		return vehicleRepository.findByVin(vin)
				.orElseThrow(() -> new VehicleNotFoundException("Vehicle not found:" + vin));

	}

	public Vehicle saveVehicle(Vehicle vehicle) {
		String vin = vehicle.getVin();
		if (vehicleRepository.findByVin(vin).isPresent()) {
			throw new VehicleAlreadyExistsException("VIN already exists: " + vin);
		}
		return vehicleRepository.save(vehicle);
	}

}
