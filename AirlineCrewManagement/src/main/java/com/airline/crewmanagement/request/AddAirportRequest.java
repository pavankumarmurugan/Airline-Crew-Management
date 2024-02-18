package com.airline.crewmanagement.request;

public class AddAirportRequest {
	
    private String airportName;
    
    private String airportCode;
	
    private String airportCountry;
	
    private String airportCity;
	
    private String airportTimeZone;

	public String getAirportName() {
		return airportName;
	}

	public void setAirportName(String airportName) {
		this.airportName = airportName;
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

	public String getAirportTimeZone() {
		return airportTimeZone;
	}

	public void setAirportTimeZone(String airportTimeZone) {
		this.airportTimeZone = airportTimeZone;
	}

	public String getAirportCode() {
		return airportCode;
	}

	public void setAirportCode(String airportCode) {
		this.airportCode = airportCode;
	}

}
