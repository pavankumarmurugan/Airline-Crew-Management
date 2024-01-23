package com.airline.crewmanagement.service;

import com.airline.crewmanagement.request.UserRegisterRequest;
import com.airline.crewmanagement.request.UserSignInRequest;
import com.airline.crewmanagement.response.UserRegisterResponse;
import com.airline.crewmanagement.response.UserSignInResponse;

public interface UserService {
	
	UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest);
	
	UserSignInResponse userSignIn(UserSignInRequest userSignInRequest);
	
}
