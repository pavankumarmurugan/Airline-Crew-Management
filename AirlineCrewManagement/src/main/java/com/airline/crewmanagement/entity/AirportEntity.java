package com.airline.crewmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name="tb003_airport_details",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = "airport_id")
    })
public class AirportEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "airport_id")
    private Long airportId;
	
	@Column(name = "airport_name", nullable=false)
    private String airportName;
	
	@Column(name = "airport_code", nullable=false)
    private String airportCode;
	
	@Column(name = "airport_country", nullable=false)
    private String airportCountry;
	
	@Column(name = "airport_city", nullable=false)
    private String airportCity;
	
	@Column(name = "airport_time_zone", nullable=false)
    private String airportTimeZone;
	
	@Column(name = "airport_status", nullable=false)
	private Boolean airportStatus = Boolean.TRUE;

	public Long getAirportId() {
		return airportId;
	}

	public void setAirportId(Long airportId) {
		this.airportId = airportId;
	}

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
	}

	public String getAirportTimeZone() {
		return airportTimeZone;
	}

	public void setAirportTimeZone(String airportTimeZone) {
		this.airportTimeZone = airportTimeZone;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getAirportCountry() {
		return airportCountry;
	}

	public void setAirportCountry(String airportCountry) {
		this.airportCountry = airportCountry;
	}

	public String getAirportCity() {
		return airportCity;
	}

	public void setAirportCity(String airportCity) {
		this.airportCity = airportCity;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

	public Boolean getAirportStatus() {
		return airportStatus;
	}

	public void setAirportStatus(Boolean airportStatus) {
		this.airportStatus = airportStatus;
	}
}
