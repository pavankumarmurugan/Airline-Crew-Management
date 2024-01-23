package com.airline.crewmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.airline.crewmanagement.request.UserRegisterRequest;
import com.airline.crewmanagement.request.UserSignInRequest;
import com.airline.crewmanagement.response.UserRegisterResponse;
import com.airline.crewmanagement.response.UserSignInResponse;
import com.airline.crewmanagement.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

	@Autowired 
	private UserService userService;
	
	@PostMapping("/registerUser")
	public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
		UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
		userRegisterResponse = userService.registerUser(userRegisterRequest);
		return ResponseEntity.ok(userRegisterResponse);

	}

	@PostMapping("/signin")
	public ResponseEntity<?> userSignin(
			@Valid @RequestBody UserSignInRequest userSignInRequest) {
		UserSignInResponse userSignInResponse = new UserSignInResponse();
		userSignInResponse = userService.userSignIn(userSignInRequest);
		return ResponseEntity.ok(userSignInResponse);
	}
}
