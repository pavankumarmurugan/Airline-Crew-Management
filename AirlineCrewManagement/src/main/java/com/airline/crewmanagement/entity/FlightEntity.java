package com.airline.crewmanagement.entity;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tb002_flight_details",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = "flight_id")
    })
public class FlightEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flight_id")
    private Long flightId;
	
	@Column(name = "flight_number", nullable=false)
    private String flightNumber;
	
	@ManyToOne
	@JoinColumn(name = "flight_departure_airport_id", nullable = false)
    private AirportEntity flightDepartureAirport;
	
	@ManyToOne
	@JoinColumn(name = "flight_destination_airport_id", nullable = false)
    private AirportEntity flightDestinationAirport;
	
	@Column(name = "flight_departure_time", nullable=false)
    private LocalTime flightDepartureTime;
	
	@Column(name = "flight_arrival_time", nullable=false)
    private LocalTime flightArrivalTime;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "tb004_flight_operating_days", joinColumns = @JoinColumn(name = "flight_id"))
	@Column(name = "flight_operating_days", nullable=false)
    private Set<String> flightOperatingDays;
	
	@Column(name = "flight_status", nullable=false)
	private Boolean flightStatus = Boolean.TRUE;
	
	@ManyToOne
	@JoinColumn(name = "aircraft_id", nullable = false)
    private AircraftEntity aircraftId;;

	
	public Long getFlightId() {
		return flightId;
	}

	public void setFlightId(Long flightId) {
		this.flightId = flightId;
	}

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public AirportEntity getFlightDepartureAirport() {
		return flightDepartureAirport;
	}

	public void setFlightDepartureAirport(AirportEntity flightDepartureAirport) {
		this.flightDepartureAirport = flightDepartureAirport;
	}

	public AirportEntity getFlightDestinationAirport() {
		return flightDestinationAirport;
	}

	public void setFlightDestinationAirport(AirportEntity flightDestinationAirport) {
		this.flightDestinationAirport = flightDestinationAirport;
	}

	public LocalTime getFlightDepartureTime() {
		return flightDepartureTime;
	}

	public void setFlightDepartureTime(LocalTime flightDepartureTime) {
		this.flightDepartureTime = flightDepartureTime;
	}

	public LocalTime getFlightArrivalTime() {
		return flightArrivalTime;
	}

	public void setFlightArrivalTime(LocalTime flightArrivalTime) {
		this.flightArrivalTime = flightArrivalTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setFlightOperatingDays(Set<DayOfWeek> operatingDays) {
        this.flightOperatingDays = operatingDays.stream()
                .map(Enum::name)
                .collect(Collectors.toSet());
    }

    public Set<DayOfWeek> getFlightOperatingDays() {
        return flightOperatingDays.stream()
                .map(DayOfWeek::valueOf)
                .collect(Collectors.toSet());
    }

	public Boolean getFlightStatus() {
		return flightStatus;
	}

	public void setFlightStatus(Boolean flightStatus) {
		this.flightStatus = flightStatus;
	}

	public AircraftEntity getAircraftId() {
		return aircraftId;
	}

	public void setAircraftId(AircraftEntity aircraftId) {
		this.aircraftId = aircraftId;
	}
}
