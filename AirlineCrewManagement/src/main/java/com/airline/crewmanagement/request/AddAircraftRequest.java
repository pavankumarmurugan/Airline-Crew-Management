package com.airline.crewmanagement.request;

public class AddAircraftRequest {
	
    private String aircraftName;
    
    private String aircraftModel;
	
	private Long aircraftSeatCapacity;

    private Long aircraftBaseLocation;

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

	public Long getAircraftBaseLocation() {
		return aircraftBaseLocation;
	}

	public void setAircraftBaseLocation(Long aircraftBaseLocation) {
		this.aircraftBaseLocation = aircraftBaseLocation;
	}

	public String getAircraftModel() {
		return aircraftModel;
	}

	public void setAircraftModel(String aircraftModel) {
		this.aircraftModel = aircraftModel;
	}
	
}
