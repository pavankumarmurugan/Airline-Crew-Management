package com.airline.crewmanagement.entity;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="tb001_user_profile",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = "user_email")
    })
public class UserEntity implements UserDetails{
	 
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "user_first_name", nullable=false)
    private String userFirstName;
    
    @Column(name = "user_last_name", nullable=false)
    private String userLastName;
    
    @Column(name = "user_email", nullable=false)
    @Email
    private String userEmail;
    
    @Column(name = "user_password", nullable=false)
    private String userPassword;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable=false)
    private Role userRole;
    
    @Column(name = "user_status", nullable=false)
    private boolean userStatus = Boolean.TRUE;
    
    @Column(name = "user_mobile_number", nullable=false)
    private String userMobileNumber;
    
    @ManyToOne
	@JoinColumn(name = "user_base_location", nullable = false)
    private AirportEntity userBaseLocation;
    
    @Column(name = "user_experience", nullable=false)
    private Long userExperience;

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

	public Role getUserRole() {
		return userRole;
	}

	public void setUserRole(Role userRole) {
		this.userRole = userRole;
	}

	public boolean getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(boolean userStatus) {
		this.userStatus = userStatus;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(userRole.name()));
	}

	@Override
	public String getPassword() {
		return userPassword;
	}

	@Override
	public String getUsername() {
		return userEmail;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return userStatus;
	}

	public AirportEntity getUserBaseLocation() {
		return userBaseLocation;
	}

	public void setUserBaseLocation(AirportEntity userBaseLocation) {
		this.userBaseLocation = userBaseLocation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserMobileNumber() {
		return userMobileNumber;
	}

	public void setUserMobileNumber(String userMobileNumber) {
		this.userMobileNumber = userMobileNumber;
	}

	public Long getUserExperience() {
		return userExperience;
	}

	public void setUserExperience(Long userExperience) {
		this.userExperience = userExperience;
	}
    
}