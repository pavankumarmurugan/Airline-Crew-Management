package com.airline.crewmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.FlightEntity;


@Repository
@Transactional
public interface FlightRepository extends JpaRepository<FlightEntity, Long> {
	
	Optional<FlightEntity> findByFlightNumber(String flightNumber);
	
	Optional<FlightEntity> findByFlightId(Long flightId);
	
	Boolean existsByFlightNumber(String flightNumber);
	
	Boolean existsByFlightId(Long flightId);
}