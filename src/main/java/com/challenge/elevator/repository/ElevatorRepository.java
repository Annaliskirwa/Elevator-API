package com.challenge.elevator.repository;

import com.challenge.elevator.entity.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Long>{
    Elevator findElevatorById(long id);
}

