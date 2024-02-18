package com.airline.crewmanagement.request;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumSet;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AddFlightRequest {
	
    private String flightNumber;
	
    private Long flightDepartureAirport;

    private Long flightDestinationAirport;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime flightDepartureTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
    private LocalTime flightArrivalTime;
	
    private EnumSet<DayOfWeek> flightOperatingDays;
    
    private Long aircraftID;

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Long getFlightDepartureAirport() {
		return flightDepartureAirport;
	}

	public void setFlightDepartureAirport(Long flightDepartureAirport) {
		this.flightDepartureAirport = flightDepartureAirport;
	}

	public Long getFlightDestinationAirport() {
		return flightDestinationAirport;
	}

	public void setFlightDestinationAirport(Long flightDestinationAirport) {
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

	public EnumSet<DayOfWeek> getFlightOperatingDays() {
		return flightOperatingDays;
	}

	public void setFlightOperatingDays(EnumSet<DayOfWeek> flightOperatingDays) {
		this.flightOperatingDays = flightOperatingDays;
	}

	public Long getAircraftID() {
		return aircraftID;
	}

	public void setAircraftID(Long aircraftID) {
		this.aircraftID = aircraftID;
	}

}
