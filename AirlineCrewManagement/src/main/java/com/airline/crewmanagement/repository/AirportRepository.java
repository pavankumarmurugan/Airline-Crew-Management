package com.airline.crewmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.AirportEntity;

@Repository
@Transactional(readOnly = true)
public interface AirportRepository extends JpaRepository<AirportEntity, Long> {
	
	Optional<AirportEntity> findByAirportId(Long airportId);
	
	Optional<AirportEntity> findByAirportName(String airportName);
	
	Boolean existsByAirportId(Long airportId);
	
	Boolean existsByAirportName(String airportName);
	
	Boolean existsByAirportCode(String airportCode);

}
