package com.airline.crewmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.airline.crewmanagement.entity.AircraftEntity;

public interface AircraftRepository extends JpaRepository<AircraftEntity, Long> {
	
	Optional<AircraftEntity> findByAircraftId(Long aircraftId);
	
	Boolean existsByAircraftId(Long aircraftId);
}
