package com.airline.crewmanagement.entity;

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
@Table(name="tb005_aircraft_details",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = "aircraft_id")
    })
public class AircraftEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "aircraft_id")
    private Long aircraftId;
	
	@Column(name = "aircraft_name", nullable=false)
    private String aircraftName;
	
	@Column(name = "aircraft_model", nullable=false)
    private String aircraftModel;
	
	@Column(name = "aircraft_seat_capacity", nullable=false)
	private Long aircraftSeatCapacity;
	
	@Column(name = "aircraft_status", nullable=false)
	private Boolean aircraftStatus = Boolean.TRUE;
	
	@ManyToOne
	@JoinColumn(name = "aircraft_base_location", nullable = false)
    private AirportEntity aircraftBaseLocation;

	public Long getAircraftId() {
		return aircraftId;
	}

	public void setAircraftId(Long aircraftId) {
		this.aircraftId = aircraftId;
	}

	public String getAircraftName() {
		return aircraftName;
	}

	public void setAircraftName(String aircraftName) {
		this.aircraftName = aircraftName;
	}

	public Long getAircraftSeatCapacity() {
		return aircraftSeatCapacity;
	}

	public void setAircraftSeatCapacity(Long aircraftSeatCapacity) {
		this.aircraftSeatCapacity = aircraftSeatCapacity;
	}

	public Boolean getAircraftStatus() {
		return aircraftStatus;
	}

	public void setAircraftStatus(Boolean aircraftStatus) {
		this.aircraftStatus = aircraftStatus;
	}

	public AirportEntity getAircraftBaseLocation() {
		return aircraftBaseLocation;
	}

	public void setAircraftBaseLocation(AirportEntity aircraftBaseLocation) {
		this.aircraftBaseLocation = aircraftBaseLocation;
	}

	public String getAircraftModel() {
		return aircraftModel;
	}

	public void setAircraftModel(String aircraftModel) {
		this.aircraftModel = aircraftModel;
	}
}
