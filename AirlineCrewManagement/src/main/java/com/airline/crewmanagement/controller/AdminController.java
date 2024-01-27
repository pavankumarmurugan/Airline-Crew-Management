package com.airline.crewmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airline.crewmanagement.request.AddAircraftRequest;
import com.airline.crewmanagement.request.AddAirportRequest;
import com.airline.crewmanagement.request.AddFlightRequest;
import com.airline.crewmanagement.request.UserRegisterRequest;
import com.airline.crewmanagement.response.UserRegisterResponse;
import com.airline.crewmanagement.service.AdminService;
import com.airline.crewmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
	
	@Autowired 
	private UserService userService;
	
	@Autowired
	private AdminService adminService;
	
	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest,
			@RequestHeader(name="Authorization") String token) {
		UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
		userRegisterResponse = userService.registerUser(userRegisterRequest, token);
		return ResponseEntity.ok(userRegisterResponse);

	}
	
	@PostMapping("/addAirport")
	public ResponseEntity<?> addAirport(@Valid @RequestBody AddAirportRequest addAirportRequest,
			@RequestHeader(name="Authorization") String token) {
		return ResponseEntity.ok(adminService.addAirport(addAirportRequest, token));
	}
	
	@PostMapping("/addAircraft")
	public ResponseEntity<?> addAircraft(@Valid @RequestBody AddAircraftRequest addAircraftRequest,
			@RequestHeader(name="Authorization") String token) {
		return ResponseEntity.ok(adminService.addAircraft(addAircraftRequest, token));
	}

	@PostMapping("/addFlight")
	public ResponseEntity<?> addFlight(@Valid @RequestBody AddFlightRequest addFlightRequest,
			@RequestHeader(name="Authorization") String token) {
		return ResponseEntity.ok(adminService.addFlight(addFlightRequest, token));
	}
	
	@GetMapping("/getFlightDetails")
	public ResponseEntity<?> getFlightDetails(
			@RequestParam("flightId") Long flightId,
			@RequestHeader(name="Authorization") String token) {
		return ResponseEntity.ok(adminService.getFlightDetails(flightId, token));
	}
	
}
