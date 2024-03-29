package com.airline.crewmanagement.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name="tb006_roaster_details",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = "roaster_id")
    })
public class RoasterEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "roaster_id")
    private Long roasterId;
	
	@Column(name = "flight_departure_date_time", nullable=false)
    private LocalDateTime flightDepartureDateTime;
	
	@Column(name = "flight_arrival_date_time", nullable=false)
    private LocalDateTime flightArrivalDateTime;
	
	@ManyToOne
	@JoinColumn(name = "flight_departure_airport_id", nullable = false)
    private AirportEntity flightDepartureAirport;
	
	@ManyToOne
	@JoinColumn(name = "flight_destination_airport_id", nullable = false)
    private AirportEntity flightDestinationAirport;
	
	@ManyToOne
	@JoinColumn(name = "flight_id", nullable = false)
    private FlightEntity flightId;
	
	@Column(name = "flight_operating_day", nullable=false)
    private String flightOperatingDay;
	
	@Column(name = "roaster_creation_date_time", nullable=false)
    private LocalDateTime roasterCreationDateTime;
	
	@Column(name = "roaster_update_date_time")
    private LocalDateTime roasterUpdateDateTime;
	
	@Column(name = "roaster_trip_status", nullable=false)
    private String roasterTripStatus;
	
	@Column(name = "roaster_comment")
    private String roasterComment;

	public Long getRoasterId() {
		return roasterId;
	}

	public void setRoasterId(Long roasterId) {
		this.roasterId = roasterId;
	}

	public LocalDateTime getFlightDepartureDateTime() {
		return flightDepartureDateTime;
	}

	public void setFlightDepartureDateTime(LocalDateTime flightDepartureDateTime) {
		this.flightDepartureDateTime = flightDepartureDateTime;
	}

	public LocalDateTime getFlightArrivalDateTime() {
		return flightArrivalDateTime;
	}

	public void setFlightArrivalDateTime(LocalDateTime flightArrivalDateTime) {
		this.flightArrivalDateTime = flightArrivalDateTime;
	}

	public FlightEntity getFlightId() {
		return flightId;
	}

	public void setFlightId(FlightEntity flightId) {
		this.flightId = flightId;
	}

	public String getFlightOperatingDay() {
		return flightOperatingDay;
	}

	public void setFlightOperatingDay(String flightOperatingDay) {
		this.flightOperatingDay = flightOperatingDay;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
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

	public LocalDateTime getRoasterCreationDateTime() {
		return roasterCreationDateTime;
	}

	public void setRoasterCreationDateTime(LocalDateTime roasterCreationDateTime) {
		this.roasterCreationDateTime = roasterCreationDateTime;
	}

	public LocalDateTime getRoasterUpdateDateTime() {
		return roasterUpdateDateTime;
	}

	public void setRoasterUpdateDateTime(LocalDateTime roasterUpdateDateTime) {
		this.roasterUpdateDateTime = roasterUpdateDateTime;
	}

	public String getRoasterTripStatus() {
		return roasterTripStatus;
	}

	public void setRoasterTripStatus(String roasterTripStatus) {
		this.roasterTripStatus = roasterTripStatus;
	}

	public String getRoasterComment() {
		return roasterComment;
	}

	public void setRoasterComment(String roasterComment) {
		this.roasterComment = roasterComment;
	}
}
