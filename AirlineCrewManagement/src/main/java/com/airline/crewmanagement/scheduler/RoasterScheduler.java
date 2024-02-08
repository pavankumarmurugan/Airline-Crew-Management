package com.airline.crewmanagement.scheduler;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

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

			Set<Long> aircraftSet = new HashSet<>();
			for(RoasterEntity roasterEntity: roasterEntityList) {		
				aircraftSet.add(roasterEntity.getFlightId().getAircraftId().getAircraftId());
			}

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

				List<UserEntity> crewAssignemntList = new ArrayList<>();
				List<UserEntity> pilotAssignemntList = new ArrayList<>();

				//				for(UserEntity crew : crewEntityList) {
				//					Boolean flag = Boolean.TRUE;
				//					for(RoasterEntity RoasterEntity : roasterEntityList) {
				//						Optional<StaffAssignmentEntity> StaffAssignmentEntityOpl = staffAssignmentRepository.findByUserIdAndRoasterId(crew, RoasterEntity);
				//						if(StaffAssignmentEntityOpl.isPresent()) {
				//							flag = Boolean.FALSE;
				//							break;
				//						}
				//					}
				//					if(flag && crewAssignemntList.size() <= requiredCrewSize) {
				//						crewAssignemntList.add(crew);
				//					}
				//				}

				int remainingCrewCapacity = requiredCrewSize - crewAssignemntList.size();
				if (remainingCrewCapacity > 0) {
					crewEntityList.stream()
					.filter(crew -> roasterEntityList.stream()
							.noneMatch(roaster -> staffAssignmentRepository.findByUserIdAndRoasterId(crew, roaster).isPresent())
							)
					.limit(remainingCrewCapacity)
					.forEach(crewAssignemntList::add);
				}

				int remainingpilotCapacity = 2 - pilotAssignemntList.size();
				if (remainingpilotCapacity > 0) {
					pilotEntityList.stream()
					.filter(pilot -> roasterEntityList.stream()
							.noneMatch(roaster -> staffAssignmentRepository.findByUserIdAndRoasterId(pilot, roaster).isPresent())
							)
					.limit(remainingpilotCapacity)
					.forEach(pilotAssignemntList::add);
				}
				//				for(UserEntity pilot : pilotEntityList) {
				//					Boolean flag = Boolean.TRUE;
				//					for(RoasterEntity RoasterEntity : roasterEntityList) {
				//						Optional<StaffAssignmentEntity> StaffAssignmentEntityOpl = staffAssignmentRepository.findByUserIdAndRoasterId(pilot, RoasterEntity);
				//						if(StaffAssignmentEntityOpl.isPresent()) {
				//							flag = Boolean.FALSE;
				//							break;
				//						}
				//					}
				//					if(flag && pilotAssignemntList.size() <= 2) {
				//						pilotAssignemntList.add(pilot);
				//					}
				//				}


				List<FlightEntity> flightEntityListForAircraft = flightRepository.findByAircraftIdAndFlightOperatingDaysContainingAndFlightStatusIsTrue(aircraftEntityOpl.get(), day.toString());

				for(FlightEntity flightEntity : flightEntityListForAircraft) {

					Optional<RoasterEntity> roasterEntityOpl = 
							roasterRepository.findByRoasterTripStatusAndFlightOperatingDayAndFlightId("Created", day.toString(), flightEntity);

					RoasterEntity roasterEntity = roasterEntityOpl.get();

					if(crewAssignemntList.size() == requiredCrewSize && pilotAssignemntList.size() == 2) {

						for (UserEntity crew : crewAssignemntList) {
							StaffAssignmentEntity staffAssignmentEntity = new StaffAssignmentEntity();
							staffAssignmentEntity.setRoasterId(roasterEntityOpl.get());
							staffAssignmentEntity.setUserId(crew);
							staffAssignmentRepository.save(staffAssignmentEntity);
						}

						for (UserEntity pilot : pilotEntityList) {
							StaffAssignmentEntity staffAssignmentEntity = new StaffAssignmentEntity();
							staffAssignmentEntity.setRoasterId(roasterEntityOpl.get());
							staffAssignmentEntity.setUserId(pilot);
							staffAssignmentRepository.save(staffAssignmentEntity);
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
