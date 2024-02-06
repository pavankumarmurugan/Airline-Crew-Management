package com.airline.crewmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.StaffAssignmentEntity;

@Repository
@Transactional
public interface StaffAssignmentRepository extends JpaRepository<StaffAssignmentEntity, Long> {

}

