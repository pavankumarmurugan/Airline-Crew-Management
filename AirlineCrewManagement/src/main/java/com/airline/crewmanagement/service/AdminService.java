package com.airline.crewmanagement.service;

import java.util.Map;

import com.airline.crewmanagement.request.AddAirportRequest;

public interface AdminService {
	
	Map<String, String> addAirport(AddAirportRequest addAirportRequest, String token);

}
