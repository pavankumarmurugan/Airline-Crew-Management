package com.airline.crewmanagement.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.airline.crewmanagement.entity.AircraftEntity;
import com.airline.crewmanagement.entity.FlightEntity;
import com.airline.crewmanagement.entity.RoasterEntity;
import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.StaffAssignmentEntity;
import com.airline.crewmanagement.entity.UserEntity;
import com.airline.crewmanagement.repository.AircraftRepository;
import com.airline.crewmanagement.repository.FlightRepository;
import com.airline.crewmanagement.repository.RoasterRepository;
import com.airline.crewmanagement.repository.StaffAssignmentRepository;
import com.airline.crewmanagement.repository.UserRepository;

//@EnableAsync
@Service
public class RoasterScheduler {

	@Autowired
	private FlightRepository flightRepository;

	@Autowired
	private RoasterRepository roasterRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StaffAssignmentRepository staffAssignmentRepository;

	@Autowired
	private AircraftRepository aircraftRepository;

	//	@Async
	@Scheduled(cron = "0 18 22 * * ?", zone = "Europe/Dublin")
	public Map<String, String> generateRoaster() {
		List<FlightEntity> flightEntityList = flightRepository.findByFlightStatusIsTrueOrderByFlightIdAsc();
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);

		for (int i = 1; i <= 7; i++) {
			for (FlightEntity flightEntity : flightEntityList) {
				ZonedDateTime flightDepartureTime = getZonedDateTime(utcDateTime, flightEntity.getFlightDepartureTime(), flightEntity.getFlightDepartureAirport().getAirportTimeZone());
				DayOfWeek departureDay = flightDepartureTime.getDayOfWeek();

				RoasterEntity roasterEntity = new RoasterEntity();
				if (flightEntity.getFlightOperatingDays().contains(departureDay)) {
					ZonedDateTime flightArrivalTime = getZonedDateTime(utcDateTime, flightEntity.getFlightArrivalTime(), flightEntity.getFlightDestinationAirport().getAirportTimeZone());

					roasterEntity = createRoasterEntity(flightEntity, flightDepartureTime, flightArrivalTime, departureDay);
					roasterRepository.save(roasterEntity);
				}

			}
			utcDateTime = utcDateTime.plusDays(1);
		}

		for (DayOfWeek day : DayOfWeek.values()) {
			List<RoasterEntity> roasterEntityList = roasterRepository.findByRoasterTripStatusAndFlightOperatingDayOrderByRoasterIdAsc("Created", day.toString());
			
			LocalDate roasterDate = LocalDate.now();
			
			if(!roasterEntityList.isEmpty()) {
				roasterDate = roasterEntityList.get(0).getFlightDepartureDateTime().toLocalDate();
			}
			
			Set<Long> aircraftSet = roasterEntityList.stream()
	                .map(roasterEntity -> roasterEntity.getFlightId().getAircraftId().getAircraftId())
	                .collect(Collectors.toSet());

			for(Long aircraftId : aircraftSet) {
				Optional<AircraftEntity> aircraftEntityOpl = aircraftRepository.findByAircraftId(aircraftId);

				List<UserEntity> crewEntityList = userRepository.findByUserBaseLocationAndUserRoleAndUserStatusIsTrue(aircraftEntityOpl.get().getAircraftBaseLocation(), Role.CREW);

				List<UserEntity> pilotEntityList = userRepository.findByUserBaseLocationAndUserRoleAndUserStatusIsTrue(aircraftEntityOpl.get().getAircraftBaseLocation(), Role.PILOT);

				int requiredCrewSize = 0;

				if (aircraftEntityOpl.get().getAircraftSeatCapacity() > 300) {
					requiredCrewSize = 10;
				} else if (aircraftEntityOpl.get().getAircraftSeatCapacity() > 200) {
					requiredCrewSize = 8;
				} else {
					requiredCrewSize = 6;
				}
				
				List<StaffAssignmentEntity> staffAssignmentEntityList = 
						staffAssignmentRepository.findByAssignmentDateAndAssignmentDay(roasterDate, day.toString());
				
				for (StaffAssignmentEntity staffAssignmentEntity : staffAssignmentEntityList) {
		            while (crewEntityList.contains(staffAssignmentEntity.getUserId())) {
		            	crewEntityList.remove(staffAssignmentEntity.getUserId());
		            }
		        }
				
				for (StaffAssignmentEntity staffAssignmentEntity : staffAssignmentEntityList) {
		            while (pilotEntityList.contains(staffAssignmentEntity.getUserId())) {
		            	pilotEntityList.remove(staffAssignmentEntity.getUserId());
		            }
		        }

				List<FlightEntity> flightEntityListForAircraft = flightRepository.findByAircraftIdAndFlightOperatingDaysContainingAndFlightStatusIsTrue(aircraftEntityOpl.get(), day.toString());

				for(FlightEntity flightEntity : flightEntityListForAircraft) {

					Optional<RoasterEntity> roasterEntityOpl = 
							roasterRepository.findByRoasterTripStatusAndFlightOperatingDayAndFlightId("Created", day.toString(), flightEntity);

					RoasterEntity roasterEntity = roasterEntityOpl.get();
						
					if(crewEntityList.size() >= requiredCrewSize && pilotEntityList.size() >= 2) {

						for (int i = 0; i < requiredCrewSize; i++) {
							generateStaffAssignmentEntity(day, crewEntityList.get(i), roasterEntity);
						}

						for (int i = 0; i < 2; i++) {
							generateStaffAssignmentEntity(day, pilotEntityList.get(i), roasterEntity);
						}
						roasterEntity.setRoasterTripStatus("Scheduled");
					} else {
						roasterEntity.setRoasterTripStatus("Cancelled");
						roasterEntity.setRoasterComment("Not Enough Crew/Pilot to assign");
					}
					roasterRepository.save(roasterEntity);
				}
			}
		}

		Map<String, String> response = new HashMap<>();
		response.put("message", "Roaster Generated Successfully");
		return response;
	}

	private void generateStaffAssignmentEntity(DayOfWeek day, UserEntity userEntity, RoasterEntity roasterEntity) {
		StaffAssignmentEntity staffAssignmentEntity = new StaffAssignmentEntity();
		staffAssignmentEntity.setRoasterId(roasterEntity);
		staffAssignmentEntity.setUserId(userEntity);
		staffAssignmentEntity.setAssignmentDate(roasterEntity.getFlightArrivalDateTime().toLocalDate());
		staffAssignmentEntity.setAssignmentDay(day.toString());
		staffAssignmentRepository.save(staffAssignmentEntity);
	}

	private ZonedDateTime getZonedDateTime(ZonedDateTime utcDateTime, LocalTime time, String timeZoneId) {
		return ZonedDateTime.of(utcDateTime.toLocalDate(), time, ZoneId.of("UTC")).withZoneSameInstant(ZoneId.of(timeZoneId));
	}

	private RoasterEntity createRoasterEntity(FlightEntity flightEntity, ZonedDateTime flightDepartureTime, ZonedDateTime flightArrivalTime, DayOfWeek departureDay) {
		RoasterEntity roasterEntity = new RoasterEntity();
		roasterEntity.setFlightId(flightEntity);
		roasterEntity.setFlightDepartureAirport(flightEntity.getFlightDepartureAirport());
		roasterEntity.setFlightDestinationAirport(flightEntity.getFlightDestinationAirport());
		roasterEntity.setFlightDepartureDateTime(flightDepartureTime.toLocalDateTime());
		roasterEntity.setFlightArrivalDateTime(flightArrivalTime.toLocalDateTime());
		roasterEntity.setFlightOperatingDay(departureDay.toString());
		roasterEntity.setRoasterTripStatus("Created");
		roasterEntity.setRoasterCreationDateTime(ZonedDateTime.now(ZoneId.of("Europe/Dublin")).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		return roasterEntity;
	}
}
