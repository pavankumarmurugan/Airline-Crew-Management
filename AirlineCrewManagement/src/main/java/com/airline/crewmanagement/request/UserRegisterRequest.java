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
    
	@NotEmpty(message = "Password should not be empty")
    private String userPassword;
	
	@NotEmpty(message = "Select profile type.")
	private String userRole;

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

}