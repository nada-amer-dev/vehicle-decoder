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
				.orElseThrow(() -> new RuntimeException("Vehicle not found:" + vin));
		
	}

}
