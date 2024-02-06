package com.airline.crewmanagement.scheduler;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.airline.crewmanagement.entity.FlightEntity;
import com.airline.crewmanagement.entity.RoasterEntity;
import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.StaffAssignmentEntity;
import com.airline.crewmanagement.entity.UserEntity;
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

	//	@Async
	@Scheduled(cron = "0 18 22 * * ?", zone = "Europe/Dublin")
	public Map<String, String> generateRoaster() {
		List<FlightEntity> flightEntityList = flightRepository.findByFlightStatusIsTrue();
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);

		for (int i = 1; i <= 7; i++) {
			for (FlightEntity flightEntity : flightEntityList) {
				ZonedDateTime flightDepartureTime = getZonedDateTime(utcDateTime, flightEntity.getFlightDepartureTime(), flightEntity.getFlightDepartureAirport().getAirportTimeZone());
				DayOfWeek departureDay = flightDepartureTime.getDayOfWeek();

				RoasterEntity roasterEntity = new RoasterEntity();
				if (flightEntity.getFlightOperatingDays().contains(departureDay)) {
					ZonedDateTime flightArrivalTime = getZonedDateTime(utcDateTime, flightEntity.getFlightArrivalTime(), flightEntity.getFlightDestinationAirport().getAirportTimeZone());

					roasterEntity = createRoasterEntity(flightEntity, flightDepartureTime, flightArrivalTime, departureDay);


					List<UserEntity> crewEntityList = userRepository.findByUserBaseLocationAndUserRoleAndUserStatusIsTrue(flightEntity.getFlightDepartureAirport(), Role.CREW);

					List<UserEntity> pilotEntityList = userRepository.findByUserBaseLocationAndUserRoleAndUserStatusIsTrue(flightEntity.getFlightDepartureAirport(), Role.PILOT);

					int requiredCrewSize = 0;
					int requiredPilotSize = 0;

					if (flightEntity.getAircraftId().getAircraftSeatCapacity() > 300) {
						requiredCrewSize = 10;
						requiredPilotSize = 2;
					} else if (flightEntity.getAircraftId().getAircraftSeatCapacity() > 200) {
						requiredCrewSize = 8;
						requiredPilotSize = 2;
					} else {
						requiredCrewSize = 6;
						requiredPilotSize = 2;
					}

					if (crewEntityList.size() >= requiredCrewSize && pilotEntityList.size() >= requiredPilotSize) {
						for (int j = 0; j < requiredCrewSize; j++) {
							StaffAssignmentEntity staffAssignmentEntity = new StaffAssignmentEntity();
							staffAssignmentEntity.setRoasterId(roasterEntity);
							staffAssignmentEntity.setUserId(crewEntityList.get(j));
							staffAssignmentRepository.save(staffAssignmentEntity);
						}

						for (int k = 0; k < requiredPilotSize; k++) {
							StaffAssignmentEntity staffAssignmentEntity = new StaffAssignmentEntity();
							staffAssignmentEntity.setRoasterId(roasterEntity);
							staffAssignmentEntity.setUserId(pilotEntityList.get(k));
							staffAssignmentRepository.save(staffAssignmentEntity);
						}
					} else {
						roasterEntity.setRoasterTripStatus(Boolean.FALSE);
					}
					roasterRepository.save(roasterEntity);
				}
			}
			utcDateTime = utcDateTime.plusDays(1);
		}

		Map<String, String> response = new HashMap<>();
		response.put("message", "Roaster Generated Successfully");
		return response;
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
		roasterEntity.setRoasterTripStatus(Boolean.TRUE);
		roasterEntity.setRoasterCreationDateTime(ZonedDateTime.now(ZoneId.of("Europe/Dublin")).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
		return roasterEntity;
	}
}
