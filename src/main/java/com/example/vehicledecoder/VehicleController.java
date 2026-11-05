package com.example.vehicledecoder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

	private final VehicleService vehicleService;

	public VehicleController(VehicleService vehicleService) {
		this.vehicleService = vehicleService;

	}

	@GetMapping("/{vin}")
	public Vehicle getVehicle(@PathVariable String vin) {

		return vehicleService.getVehicleByVin(vin);
	}

	@PostMapping
	public Vehicle createVehicle(@RequestBody Vehicle vehicle) {
		return vehicleService.saveVehicle(vehicle);
	}
}