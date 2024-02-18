package com.airline.crewmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.airline.crewmanagement.scheduler.RoasterScheduler;

@RestController
@RequestMapping("/api/v1/auth")
public class SchedulerController {
	
	@Autowired
	private RoasterScheduler roasterScheduler;
	
	@GetMapping("/generateRoaster")
	public ResponseEntity<?> generateRoaster() {
		return ResponseEntity.ok(roasterScheduler.generateRoaster());
	}

}
