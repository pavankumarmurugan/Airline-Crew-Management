package com.airline.crewmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.FlightEntity;
import com.airline.crewmanagement.entity.RoasterEntity;

@Repository
@Transactional
public interface RoasterRepository extends JpaRepository<RoasterEntity, Long> {
	
	List<RoasterEntity> findByRoasterTripStatusAndFlightOperatingDayOrderByRoasterIdAsc(String roasterTripStatus, String flightOperatingDay);
	
	Optional<RoasterEntity> findByRoasterTripStatusAndFlightOperatingDayAndFlightId(String roasterTripStatus,String flightOperatingDay,FlightEntity flightEntity);

}
