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

		return vehicleRepository.findByVin(vin).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found:" + vin));

	}

	public Vehicle saveVehicle(Vehicle vehicle) {
		return vehicleRepository.save(vehicle);
	}

}
