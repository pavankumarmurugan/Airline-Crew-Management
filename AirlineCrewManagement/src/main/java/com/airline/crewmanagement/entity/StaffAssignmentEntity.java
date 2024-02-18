package com.airline.crewmanagement.entity;

import java.time.LocalDate;

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
@Table(name="tb007_staff_assignment_details",
		uniqueConstraints = {
        @UniqueConstraint(columnNames = "assignment_id")
    })
public class StaffAssignmentEntity {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "assignment_id")
    private Long assignmentId;
	
	@ManyToOne
	@JoinColumn(name = "roaster_id", nullable = false)
    private RoasterEntity roasterId;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
    private UserEntity userId;
	
	@Column(name = "assignment_date")
    private LocalDate assignmentDate;
	
	@Column(name = "assignment_day")
    private String assignmentDay;

	public Long getAssignmentId() {
		return assignmentId;
	}

	public void setAssignmentId(Long assignmentId) {
		this.assignmentId = assignmentId;
	}

	public RoasterEntity getRoasterId() {
		return roasterId;
	}

	public void setRoasterId(RoasterEntity roasterId) {
		this.roasterId = roasterId;
	}

	public UserEntity getUserId() {
		return userId;
	}

	public void setUserId(UserEntity userId) {
		this.userId = userId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public LocalDate getAssignmentDate() {
		return assignmentDate;
	}

	public void setAssignmentDate(LocalDate assignmentDate) {
		this.assignmentDate = assignmentDate;
	}

	public String getAssignmentDay() {
		return assignmentDay;
	}

	public void setAssignmentDay(String assignmentDay) {
		this.assignmentDay = assignmentDay;
	}

}
