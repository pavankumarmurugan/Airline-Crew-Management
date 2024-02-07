package com.airline.crewmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.StaffAssignmentEntity;
import com.airline.crewmanagement.entity.UserEntity;

@Repository
@Transactional
public interface StaffAssignmentRepository extends JpaRepository<StaffAssignmentEntity, Long> {
	
	List<StaffAssignmentEntity> findByUserId(UserEntity userId);

}

