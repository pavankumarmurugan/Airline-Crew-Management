package com.airline.crewmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.crewmanagement.service.CrewService;

@RestController
@RequestMapping("/api/v1/crew")
public class CrewController {
	
	@Autowired
	CrewService crewService;
	
	@GetMapping("/allCrewDetails")
	public ResponseEntity<?> getAllCrewDetails() {
		return ResponseEntity.ok(crewService.getAllCrewDetails());
	}

	
}
