package com.airline.crewmanagement.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {

	@NotEmpty(message = "First Name should not be empty")
    private String userFirstName;
    
	@NotEmpty(message = "Last Name should not be empty")
    private String userLastName;
    
	@NotEmpty(message = "Email should not be empty")
    @Email(message = "Email is not valid")
    private String userEmail;
	
	@NotEmpty(message = "Select profile type")
	private String userRole;
	
	private String userMobileNumber;
	
	private Long userBaseLocation;
	
	private Long userExperience;

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

	public Long getUserBaseLocation() {
		return userBaseLocation;
	}

	public void setUserBaseLocation(Long userBaseLocation) {
		this.userBaseLocation = userBaseLocation;
	}

	public Long getUserExperience() {
		return userExperience;
	}

	public void setUserExperience(Long userExperience) {
		this.userExperience = userExperience;
	}

}