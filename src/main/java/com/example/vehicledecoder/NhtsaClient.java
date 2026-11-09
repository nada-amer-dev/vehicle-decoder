package com.example.vehicledecoder;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class NhtsaClient {

	private final RestClient restClient;

	public NhtsaClient(RestClient.Builder builder) {
		this.restClient = builder.baseUrl("https://vpic.nhtsa.dot.gov/api/vehicles").build();
	}

	public NhtsaResult decode(String vin) {

		NhtsaResponse response = restClient.get().uri("/DecodeVinValues/{vin}?format=json", vin).retrieve()
				.body(NhtsaResponse.class);

		if (response == null || response.results() == null || response.results().isEmpty()) {
			throw new VehicleNotFoundException("Could not decode VIN: " + vin);
		}

		return response.results().get(0);
	}

}
