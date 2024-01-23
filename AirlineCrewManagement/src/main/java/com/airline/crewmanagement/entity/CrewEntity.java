package com.airline.crewmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name="tb002_crew_profile",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "crew_id")
        })
public class CrewEntity {
	
	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "crew_id")
    private Long crewId;

    @OneToOne
    @JoinColumn(
            nullable = false,
            name = "user_id"
    )
    private UserEntity userId;
    
    @Column(name = "crew_mobile_no")
    private String crewMobileNo;
    
    @Column(name = "crew_base_location")
    private String crewBaseLocation;

	public Long getCrewId() {
		return crewId;
	}

	public void setCrewId(Long crewId) {
		this.crewId = crewId;
	}

	public UserEntity getUserId() {
		return userId;
	}

	public void setUserId(UserEntity userId) {
		this.userId = userId;
	}

	public String getCrewMobileNo() {
		return crewMobileNo;
	}

	public void setCrewMobileNo(String crewMobileNo) {
		this.crewMobileNo = crewMobileNo;
	}

	public String getCrewBaseLocation() {
		return crewBaseLocation;
	}

	public void setCrewBaseLocation(String crewBaseLocation) {
		this.crewBaseLocation = crewBaseLocation;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
