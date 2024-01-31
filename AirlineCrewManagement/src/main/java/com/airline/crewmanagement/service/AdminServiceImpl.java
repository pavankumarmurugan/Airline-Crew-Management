package com.airline.crewmanagement.service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.airline.crewmanagement.entity.AircraftEntity;
import com.airline.crewmanagement.entity.AirportEntity;
import com.airline.crewmanagement.entity.FlightEntity;
import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.UserEntity;
import com.airline.crewmanagement.repository.AircraftRepository;
import com.airline.crewmanagement.repository.AirportRepository;
import com.airline.crewmanagement.repository.FlightRepository;
import com.airline.crewmanagement.repository.UserRepository;
import com.airline.crewmanagement.request.AddAircraftRequest;
import com.airline.crewmanagement.request.AddAirportRequest;
import com.airline.crewmanagement.request.AddFlightRequest;

@Service
public class AdminServiceImpl implements AdminService {
	
	@Autowired
	private AirportRepository airportRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtService jwtService;
	
	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private AircraftRepository aircraftRepository;

	@Override
	public Map<String, String> addAirport(AddAirportRequest addAirportRequest, String token) {
		
		checkUser(token);
		
		if (Boolean.TRUE.equals(airportRepository.existsByAirportName(addAirportRequest.getAirportName()))) {
		    throw new IllegalArgumentException("Airport Name is already in records!");
		}
		
		if (Boolean.TRUE.equals(airportRepository.existsByAirportCode(addAirportRequest.getAirportCode()))) {
		    throw new IllegalArgumentException("Airport Code is already in records!");
		}
		
		Set<String> availableIDs = ZoneId.getAvailableZoneIds();
		
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

	@Override
	public Map<String, String> addFlight(AddFlightRequest addFlightRequest, String token) {
		
		checkUser(token);
		
		if(Boolean.TRUE.equals(flightRepository.existsByFlightNumber(addFlightRequest.getFlightNumber()))) {
			throw new IllegalArgumentException("Flight Number already in records!");
		}
		
		Optional<AirportEntity> flightDepartureAirportEntityOpt = airportRepository.findByAirportIdAndAirportStatusIsTrue(addFlightRequest.getFlightDepartureAirport());
		
		if (flightDepartureAirportEntityOpt.isEmpty()) {
		    throw new IllegalArgumentException("Flight Departure Airport is not in records!");
		}
		
		Optional<AirportEntity> flightDestinationAirportEntityOpt =  airportRepository.findByAirportIdAndAirportStatusIsTrue(addFlightRequest.getFlightDestinationAirport());
		
		if (flightDestinationAirportEntityOpt.isEmpty()) {
		    throw new IllegalArgumentException("Flight Destination Airport is not in records!");
		}
		
		Optional<AircraftEntity> aircraftEntityOpt = aircraftRepository.findByAircraftIdAndAircraftStatusIsTrue(addFlightRequest.getAircraftID());
		
		if (aircraftEntityOpt.isEmpty()) {
		    throw new IllegalArgumentException("Aircraft is not in records!");
		}
		
		ZonedDateTime departureZoneCurrentDate = ZonedDateTime.now(ZoneId.of(flightDestinationAirportEntityOpt.get().getAirportTimeZone()));
		
		ZonedDateTime departureZoneDateTime = ZonedDateTime.of(LocalDateTime.of(departureZoneCurrentDate.getYear(), 
				departureZoneCurrentDate.getMonth(), departureZoneCurrentDate.getDayOfMonth(), addFlightRequest.getFlightDepartureTime().getHour(), 
				addFlightRequest.getFlightDepartureTime().getMinute()), ZoneId.of(flightDepartureAirportEntityOpt.get().getAirportTimeZone()));
		
		ZonedDateTime utcDepartureDateTime = departureZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		
		LocalTime flightDepartureTime = LocalTime.of(utcDepartureDateTime.getHour(), utcDepartureDateTime.getMinute());
		
		ZonedDateTime arrivalZoneCurrentDate = ZonedDateTime.now(ZoneId.of(flightDestinationAirportEntityOpt.get().getAirportTimeZone()));
		
		ZonedDateTime arrivalZoneDateTime = ZonedDateTime.of(LocalDateTime.of(arrivalZoneCurrentDate.getYear(), 
				arrivalZoneCurrentDate.getMonth(), arrivalZoneCurrentDate.getDayOfMonth(), addFlightRequest.getFlightArrivalTime().getHour(), 
				addFlightRequest.getFlightArrivalTime().getMinute()), ZoneId.of(flightDestinationAirportEntityOpt.get().getAirportTimeZone()));
		
		ZonedDateTime utcArrivalDateTime = arrivalZoneDateTime.withZoneSameInstant(ZoneId.of("UTC"));
		
		LocalTime flightArrivalTime = LocalTime.of(utcArrivalDateTime.getHour(), utcArrivalDateTime.getMinute());
		
		FlightEntity flightEntity = new FlightEntity();
		flightEntity.setFlightNumber(addFlightRequest.getFlightNumber());
		flightEntity.setFlightDepartureAirport(flightDepartureAirportEntityOpt.get());
		flightEntity.setFlightDestinationAirport(flightDestinationAirportEntityOpt.get());
		flightEntity.setFlightDepartureTime(flightDepartureTime);
		flightEntity.setFlightArrivalTime(flightArrivalTime);
		flightEntity.setFlightOperatingDays(addFlightRequest.getFlightOperatingDays());
		flightEntity.setAircraftId(aircraftEntityOpt.get());
		
		
		flightRepository.save(flightEntity);
		
		Map<String, String> response = new HashMap<>();
		response.put("message", "Flight added successfully");

		return response;
	}

	@Override
	public FlightEntity getFlightDetails(Long flightId, String token) {
		
		checkUser(token);
		
		Optional<FlightEntity> flightEntityOpt = flightRepository.findByFlightId(flightId);
		
		if(flightEntityOpt.isEmpty()) {
			throw new IllegalArgumentException("Flight Number is not in records!");
		}
		
		FlightEntity flightEntity = flightEntityOpt.get();
		
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		
		ZonedDateTime flightDepartureTime = ZonedDateTime.of(LocalDateTime.of(utcDateTime.toLocalDate().getYear(), 
				utcDateTime.toLocalDate().getMonth(), utcDateTime.toLocalDate().getDayOfMonth(), flightEntity.getFlightDepartureTime().getHour(), 
				flightEntity.getFlightDepartureTime().getMinute()), ZoneId.of("UTC"));
		
		ZoneId departureZoneId = ZoneId.of(flightEntity.getFlightDepartureAirport().getAirportTimeZone());
	     
	    flightEntity.setFlightDepartureTime(flightDepartureTime.withZoneSameInstant(departureZoneId).toLocalTime());
	    
	    ZonedDateTime flightArrivalTime = ZonedDateTime.of(LocalDateTime.of(utcDateTime.toLocalDate().getYear(), 
				utcDateTime.toLocalDate().getMonth(), utcDateTime.toLocalDate().getDayOfMonth(), flightEntity.getFlightArrivalTime().getHour(), 
				flightEntity.getFlightArrivalTime().getMinute()), ZoneId.of("UTC"));
	    
	    ZoneId arrivalZoneId = ZoneId.of(flightEntity.getFlightDestinationAirport().getAirportTimeZone());
	    
	    flightEntity.setFlightArrivalTime(flightArrivalTime.withZoneSameInstant(arrivalZoneId).toLocalTime());
		
		return flightEntity;
	}
	
	@Override
	public List<FlightEntity> getAllFlightDetails(String token) {
		
		checkUser(token);
		
		List<FlightEntity> responseFlightEntityList = new ArrayList<FlightEntity>();
		
		List<FlightEntity> flightEntityList = flightRepository.findAll();
		
		for(FlightEntity flightEntity : flightEntityList) {
			
			ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
			
			ZonedDateTime flightDepartureTime = ZonedDateTime.of(LocalDateTime.of(utcDateTime.toLocalDate().getYear(), 
					utcDateTime.toLocalDate().getMonth(), utcDateTime.toLocalDate().getDayOfMonth(), flightEntity.getFlightDepartureTime().getHour(), 
					flightEntity.getFlightDepartureTime().getMinute()), ZoneId.of("UTC"));
			
			ZoneId departureZoneId = ZoneId.of(flightEntity.getFlightDepartureAirport().getAirportTimeZone());
		     
		    flightEntity.setFlightDepartureTime(flightDepartureTime.withZoneSameInstant(departureZoneId).toLocalTime());
		    
		    ZonedDateTime flightArrivalTime = ZonedDateTime.of(LocalDateTime.of(utcDateTime.toLocalDate().getYear(), 
					utcDateTime.toLocalDate().getMonth(), utcDateTime.toLocalDate().getDayOfMonth(), flightEntity.getFlightArrivalTime().getHour(), 
					flightEntity.getFlightArrivalTime().getMinute()), ZoneId.of("UTC"));
		    
		    ZoneId arrivalZoneId = ZoneId.of(flightEntity.getFlightDestinationAirport().getAirportTimeZone());
		    
		    flightEntity.setFlightArrivalTime(flightArrivalTime.withZoneSameInstant(arrivalZoneId).toLocalTime());
			
		    responseFlightEntityList.add(flightEntity);
		}
		
		return responseFlightEntityList;
	}

	@Override
	public Map<String, String> addAircraft(AddAircraftRequest addAircraftRequest, String token) {
		

		checkUser(token);
		
		Optional<AirportEntity> airportEntityOpt = airportRepository.findByAirportIdAndAirportStatusIsTrue(
				addAircraftRequest.getAircraftBaseLocation());
		
		if (airportEntityOpt.isEmpty()) {
		    throw new IllegalArgumentException("Base Location is not in records!");
		}
		
		AircraftEntity aircraftEntity = new AircraftEntity();
		aircraftEntity.setAircraftName(addAircraftRequest.getAircraftName());
		aircraftEntity.setAircraftModel(addAircraftRequest.getAircraftModel());
		aircraftEntity.setAircraftSeatCapacity(addAircraftRequest.getAircraftSeatCapacity());
		aircraftEntity.setAircraftBaseLocation(airportEntityOpt.get());
		
		aircraftRepository.save(aircraftEntity);
		
		Map<String, String> response = new HashMap<>();
		response.put("message", "Aircraft added successfully");

		return response;
	}
	
	private void checkUser(String token) {
		
		String userEmail = jwtService.extractUsername(token.substring(7));
		
		Optional<UserEntity> userEntity =  userRepository.findByUserEmailAndUserRoleAndUserStatusIsTrue(userEmail, Role.ADMIN);

		if (userEntity.isEmpty()) {
			throw new IllegalArgumentException("User is not Valid");
		}
	}

}
