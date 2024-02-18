package com.airline.crewmanagement.response;

import com.airline.crewmanagement.entity.AirportEntity;

public class UserRegisterResponse {
	
	private Long userId;
	
    private String userFirstName;
    
    private String userLastName;
    
    private String userEmail;
    
    private String userPassword;
	
	private String userRole;
	
	private String userMobileNumber;
	
	private AirportEntity userBaseLocation;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserFirstName() {
		return userFirstName;
	}

	public void setUserFirstName(String userFirstName) {
		this.userFirstName = userFirstName;
	}

	public String getUserLastName() {
		return userLastName;
	}

	public void setUserLastName(String userLastName) {
		this.userLastName = userLastName;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	public AirportEntity getUserBaseLocation() {
		return userBaseLocation;
	}

	public void setUserBaseLocation(AirportEntity userBaseLocation) {
		this.userBaseLocation = userBaseLocation;
	}

}
