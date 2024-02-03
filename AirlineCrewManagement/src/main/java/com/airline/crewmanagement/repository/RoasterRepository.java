package com.airline.crewmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.airline.crewmanagement.entity.RoasterEntity;

@Repository
@Transactional
public interface RoasterRepository extends JpaRepository<RoasterEntity, Long> {

}
