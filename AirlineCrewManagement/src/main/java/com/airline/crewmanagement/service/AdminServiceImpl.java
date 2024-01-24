package com.airline.crewmanagement.service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.crewmanagement.entity.AirportEntity;
import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.UserEntity;
import com.airline.crewmanagement.repository.AirportRepository;
import com.airline.crewmanagement.repository.UserRepository;
import com.airline.crewmanagement.request.AddAirportRequest;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AirportRepository airportRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;

	@Override
	public Map<String, String> addAirport(AddAirportRequest addAirportRequest, String token) {
		
		Optional<UserEntity> userEntity =  userRepository.findByUserEmailAndUserRole(jwtService.extractUsername(token.substring(7)), Role.ADMIN);

		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException("User is not Valid");
		}
		
		if (Boolean.TRUE.equals(airportRepository.existsByAirportName(addAirportRequest.getAirportName()))) {
		    throw new IllegalArgumentException("Airport is already in records!");
		}
		
		if (Boolean.TRUE.equals(airportRepository.existsByAirportCode(addAirportRequest.getAirportCode()))) {
		    throw new IllegalArgumentException("Airport Code is already in records!");
		}
		
		String[] availableIDs = TimeZone.getAvailableIDs();
		
		boolean timezoneFound = false;
        for (String timeZoneID : availableIDs) {
            if (timeZoneID.equalsIgnoreCase(addAirportRequest.getAirportTimeZone())) {
            	timezoneFound = true;
                break;
            }
        }

        if (!timezoneFound) {
        	throw new IllegalArgumentException("Airport Timezone is not valid");
        }
        
        boolean countryFound = false;
        String[] locales = Locale.getISOCountries();

    	for (String countryCode : locales) {
    		Locale obj = new Locale("", countryCode);
    		if (obj.getDisplayCountry().equalsIgnoreCase(addAirportRequest.getAirportCountry())) {
    			countryFound = true;
                break;
            }
    	}
    	
    	if (!countryFound) {
        	throw new IllegalArgumentException("Airport Country is not valid");
        }

		AirportEntity airportEntity = new AirportEntity();
		airportEntity.setAirportCity(addAirportRequest.getAirportCity());
		airportEntity.setAirportCountry(addAirportRequest.getAirportCountry());
		airportEntity.setAirportName(addAirportRequest.getAirportName());
		airportEntity.setAirportTimeZone(addAirportRequest.getAirportTimeZone());
		airportEntity.setAirportCode(addAirportRequest.getAirportCode());
		
		airportRepository.save(airportEntity);

		Map<String, String> response = new HashMap<>();
		response.put("message", "Airport added successfully");

		return response;
	}

}
