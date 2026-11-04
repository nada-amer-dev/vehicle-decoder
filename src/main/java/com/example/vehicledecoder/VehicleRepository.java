package com.example.vehicledecoder;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
	
	Optional<Vehicle> findByVin(String vin);

}
