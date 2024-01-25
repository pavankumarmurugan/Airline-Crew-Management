package com.airline.crewmanagement.service;

import java.util.Map;

import com.airline.crewmanagement.entity.FlightEntity;
import com.airline.crewmanagement.request.AddAirportRequest;
import com.airline.crewmanagement.request.AddFlightRequest;

public interface AdminService {
	
	Map<String, String> addAirport(AddAirportRequest addAirportRequest, String token);
	
	Map<String, String> addFlight(AddFlightRequest addFlightRequest, String token);
	
	FlightEntity getFlightDetails(Long flightId, String token);

}
