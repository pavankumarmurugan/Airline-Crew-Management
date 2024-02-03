package com.airline.crewmanagement.scheduler;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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
import com.airline.crewmanagement.repository.FlightRepository;
import com.airline.crewmanagement.repository.RoasterRepository;

//@EnableAsync
@Service
public class RoasterScheduler {

	@Autowired
	private FlightRepository flightRepository;
	
	@Autowired
	private RoasterRepository roasterRepository;

	//	@Async
	@Scheduled(cron = "0 18 22 * * ?", zone = "Europe/Dublin")
	public Map<String, String> generateRoaster() {

		List<FlightEntity> flightEntityList = flightRepository.findByFlightStatusIsTrue();

		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(3);

		for(int i = 1; i <= 7; i++) {
			for(FlightEntity flightEntity : flightEntityList) {

				ZonedDateTime flightDepartureTime = ZonedDateTime.of(LocalDateTime.of(utcDateTime.toLocalDate().getYear(), 
						utcDateTime.toLocalDate().getMonth(), utcDateTime.toLocalDate().getDayOfMonth(), flightEntity.getFlightDepartureTime().getHour(), 
						flightEntity.getFlightDepartureTime().getMinute()), ZoneId.of("UTC"));

				ZoneId departureZoneId = ZoneId.of(flightEntity.getFlightDepartureAirport().getAirportTimeZone());

				DayOfWeek departureDay = flightDepartureTime.withZoneSameInstant(departureZoneId).getDayOfWeek();

				if(flightEntity.getFlightOperatingDays().contains(departureDay)) {
					ZonedDateTime flightArrivalTime = ZonedDateTime.of(LocalDateTime.of(utcDateTime.toLocalDate().getYear(), 
							utcDateTime.toLocalDate().getMonth(), utcDateTime.toLocalDate().getDayOfMonth(), flightEntity.getFlightArrivalTime().getHour(), 
							flightEntity.getFlightArrivalTime().getMinute()), ZoneId.of("UTC"));

					ZoneId arrivalZoneId = ZoneId.of(flightEntity.getFlightDestinationAirport().getAirportTimeZone());

					DayOfWeek arrivalDay = flightArrivalTime.withZoneSameInstant(arrivalZoneId).getDayOfWeek();

					System.out.println(flightEntity.getFlightNumber() + " D: " + departureDay.toString() + ": "+ flightDepartureTime.withZoneSameInstant(departureZoneId).toLocalDateTime()
							+ " ");

					System.out.println(flightEntity.getFlightNumber() + " A: " + arrivalDay.toString() + ": "+ flightArrivalTime.withZoneSameInstant(arrivalZoneId).toLocalDateTime()
							+ " ");
					
					RoasterEntity roasterEntity = new RoasterEntity();
					
					roasterEntity.setFlightId(flightEntity);
					roasterEntity.setFlightDepartureAirport(flightEntity.getFlightDepartureAirport());
					roasterEntity.setFlightDestinationAirport(flightEntity.getFlightDestinationAirport());
					roasterEntity.setFlightDepartureDateTime(flightDepartureTime.withZoneSameInstant(departureZoneId).toLocalDateTime());
					roasterEntity.setFlightArrivalDateTime(flightArrivalTime.withZoneSameInstant(arrivalZoneId).toLocalDateTime());
					roasterEntity.setFlightOperatingDay(departureDay.toString());
					roasterEntity.setRoasterTripStatus(Boolean.TRUE);
					roasterEntity.setRoasterCreationDateTime(ZonedDateTime.now(ZoneId.of("Europe/Dublin")).withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
					
					roasterRepository.save(roasterEntity);
				}
			}
			utcDateTime = utcDateTime.plusDays(1);
		}

		Map<String, String> response = new HashMap<>();
		response.put("message", "Roaster Generated Successfully");

		return response;
	}

}
