package com.airline.crewmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.AircraftEntity;
import com.airline.crewmanagement.entity.FlightEntity;


@Repository
@Transactional
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
	
	Optional<FlightEntity> findByFlightNumber(String flightNumber);
	
	Optional<FlightEntity> findByFlightIdAndFlightStatusIsTrue(Long flightId);
	
	Optional<FlightEntity> findByFlightId(Long flightId);
	
	List<FlightEntity> findByAircraftId(AircraftEntity aircraftEntity);
	
	List<FlightEntity> findByAircraftIdAndFlightOperatingDaysContainingAndFlightStatusIsTrue(AircraftEntity aircraft, String day);
	
	Boolean existsByFlightNumber(String flightNumber);
	
	Boolean existsByFlightId(Long flightId);
}