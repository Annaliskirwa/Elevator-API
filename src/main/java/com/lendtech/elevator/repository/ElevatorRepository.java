package com.lendtech.elevator.repository;

import com.lendtech.elevator.entity.Elevator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElevatorRepository extends JpaRepository<Elevator, Long>{
    Elevator findElevatorById(long id);
}

