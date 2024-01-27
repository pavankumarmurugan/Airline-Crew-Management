package com.airline.crewmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.Role;
import com.airline.crewmanagement.entity.UserEntity;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<UserEntity, Long> {
	
	Optional<UserEntity> findByUserEmail(String userEmail);
	
	Optional<UserEntity> findByUserEmailAndUserRole(String userEmail, Role role);
	
	Optional<UserEntity> findByUserEmailAndUserRoleAndUserStatusIsTrue(String userEmail, Role role);
	
	Boolean existsByUserEmail(String email);
}
