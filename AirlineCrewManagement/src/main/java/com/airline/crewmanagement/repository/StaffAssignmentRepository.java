package com.airline.crewmanagement.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.RoasterEntity;
import com.airline.crewmanagement.entity.StaffAssignmentEntity;
import com.airline.crewmanagement.entity.UserEntity;

@Repository
@Transactional
public interface StaffAssignmentRepository extends JpaRepository<StaffAssignmentEntity, Long> {
	
	List<StaffAssignmentEntity> findByUserId(UserEntity userId);
	
	Optional<StaffAssignmentEntity> findByUserIdAndRoasterId(UserEntity userId, RoasterEntity roasterId);
	
	List<StaffAssignmentEntity> findByUserIdAndAssignmentDateAndAssignmentDay(UserEntity userId, LocalDate assignmentDate, String assignmentDay);
	
	List<StaffAssignmentEntity> findByAssignmentDateAndAssignmentDay(LocalDate assignmentDate, String assignmentDay);

}

