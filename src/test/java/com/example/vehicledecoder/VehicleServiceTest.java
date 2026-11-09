package com.example.vehicledecoder;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VehicleServiceTest {
	@Mock
	private VehicleRepository vehicleRepository;
	@Mock
	private NhtsaClient nhtsaClient;
	@InjectMocks
	private VehicleService vehicleService;

	@Test
	void returnsSavedVehicleWhenVinExists() {
		// Arrange: a vehicle, and tell the fake repository to return it

		Vehicle vehicle = new Vehicle();
		vehicle.setVin("1HGCM82633A004352");
		when(vehicleRepository.findByVin("1HGCM82633A004352")).thenReturn(Optional.of(vehicle));

		// Act: call the service

		Vehicle result = vehicleService.getVehicleByVin("1HGCM82633A004352");

		// Assert: same vehicle returned, NHTSA never called

		assertSame(vehicle, result);
		verify(nhtsaClient, never()).decode(any());
	}

}
