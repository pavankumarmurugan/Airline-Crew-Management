package com.airline.crewmanagement.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.UserEntity;
import com.airline.crewmanagement.repository.AirportRepository;
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
	private AirportRepository airportRepository;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtService jwtService;
	
	@Override
	public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest, String token) {
		
		Optional<UserEntity> userEntity =  userRepository.findByUserEmailAndUserRole(jwtService.extractUsername(token.substring(7)), Role.ADMIN);

		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException("User is not Valid");
		}
		
		if (Boolean.TRUE.equals(userRepository.existsByUserEmail(userRegisterRequest.getUserEmail()))) {
		    throw new IllegalArgumentException("Email is already in use!");
		}
		
		if (Boolean.FALSE.equals(airportRepository.existsByAirportId(userRegisterRequest.getUserBaseLocation()))) {
		    throw new IllegalArgumentException("Not valid User Base Location");
		}
        
		UserEntity user = new UserEntity();
		
        user.setUserFirstName(userRegisterRequest.getUserFirstName());
        user.setUserLastName(userRegisterRequest.getUserLastName());
        user.setUserEmail(userRegisterRequest.getUserEmail());
        String password = generateCommonLangPassword();
        user.setUserPassword(passwordEncoder.encode(password));
        System.out.println("Password: " + password);
        
        if(userRegisterRequest.getUserRole().equals("CREW")) {
        	user.setUserRole(Role.CREW);
        } else if(userRegisterRequest.getUserRole().equals("PILOT")) {
        	user.setUserRole(Role.PILOT);
        } else if(userRegisterRequest.getUserRole().equals("MANAGER")) {
        	user.setUserRole(Role.MANAGER);
        } else {
        	throw new IllegalArgumentException("Not valid User Role");
        }
        
        user.setUserMobileNumber(userRegisterRequest.getUserMobileNumber());
        user.setUserBaseLocation(airportRepository.findByAirportId(userRegisterRequest.getUserBaseLocation()).get());        
        
        userRepository.save(user);
        
        return mapToUserRegisterResponse(user);
		
	}

	private UserRegisterResponse mapToUserRegisterResponse(UserEntity user){
		UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
		userRegisterResponse.setUserFirstName(user.getUserFirstName());
		userRegisterResponse.setUserLastName(user.getUserLastName());
		userRegisterResponse.setUserEmail(user.getUserEmail());
		userRegisterResponse.setUserRole(user.getUserRole().name());
		userRegisterResponse.setUserBaseLocation(user.getUserBaseLocation());
		userRegisterResponse.setUserMobileNumber(user.getUserMobileNumber());
		userRegisterResponse.setMessage("User registered successfully!");
        return userRegisterResponse;
    }
	
	public String generateCommonLangPassword() {
	    String upperCaseLetters = RandomStringUtils.random(2, 65, 90, true, true);
	    String lowerCaseLetters = RandomStringUtils.random(2, 97, 122, true, true);
	    String numbers = RandomStringUtils.randomNumeric(2);
	    String specialChar = RandomStringUtils.random(2, 33, 47, false, false);
	    String totalChars = RandomStringUtils.randomAlphanumeric(2);
	    String combinedChars = upperCaseLetters.concat(lowerCaseLetters)
	      .concat(numbers)
	      .concat(specialChar)
	      .concat(totalChars);
	    List<Character> pwdChars = combinedChars.chars()
	      .mapToObj(c -> (char) c)
	      .collect(Collectors.toList());
	    Collections.shuffle(pwdChars);
	    String password = pwdChars.stream()
	      .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
	      .toString();
	    return password;
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