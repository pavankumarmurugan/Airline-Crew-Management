package com.airline.crewmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.UserEntity;
import com.airline.crewmanagement.repository.UserRepository;
import com.airline.crewmanagement.request.UserRegisterRequest;
import com.airline.crewmanagement.request.UserSignInRequest;
import com.airline.crewmanagement.response.UserRegisterResponse;
import com.airline.crewmanagement.response.UserSignInResponse;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
    private UserRepository userRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {

		if (Boolean.TRUE.equals(userRepository.existsByUserEmail(userRegisterRequest.getUserEmail()))) {
		    throw new IllegalArgumentException("Email is already in use!");
		}
        
		UserEntity user = new UserEntity();
		
        user.setUserFirstName(userRegisterRequest.getUserFirstName());
        user.setUserLastName(userRegisterRequest.getUserLastName());
        user.setUserEmail(userRegisterRequest.getUserEmail());
        user.setUserPassword(passwordEncoder.encode(userRegisterRequest.getUserPassword()));
        
        if(userRegisterRequest.getUserRole().equals("CREW")) {
        	user.setUserRole(Role.CREW);
        } else if(userRegisterRequest.getUserRole().equals("PILOT")) {
        	user.setUserRole(Role.PILOT);
        } else if(userRegisterRequest.getUserRole().equals("MANAGER")) {
        	user.setUserRole(Role.MANAGER);
        }
        
        userRepository.save(user);
        
        return mapToUserRegisterResponse(user);
		
	}

	private UserRegisterResponse mapToUserRegisterResponse(UserEntity user){
		UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
		userRegisterResponse.setUserFirstName(user.getUserFirstName());
		userRegisterResponse.setUserLastName(user.getUserLastName());
		userRegisterResponse.setUserEmail(user.getUserEmail());
		userRegisterResponse.setUserRole(user.getUserRole().name());
		userRegisterResponse.setMessage("User registered successfully!");
        return userRegisterResponse;
    }

	@Override
	public UserSignInResponse userSignIn(UserSignInRequest userSignInRequest) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userSignInRequest.getUserEmail(), userSignInRequest.getUserPassword()));
		var user = userRepository.findByUserEmail(userSignInRequest.getUserEmail()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		UserSignInResponse userSignInResponse = new UserSignInResponse();
		userSignInResponse.setToken(jwtToken);
		userSignInResponse.setMessage("Login Successfull!!");
		userSignInResponse.setUserRole(user.getUserRole().toString());
		userSignInResponse.setUserEmail(user.getUserEmail());
		userSignInResponse.setUserFirstName(user.getUserFirstName());
		return userSignInResponse;
	}
}