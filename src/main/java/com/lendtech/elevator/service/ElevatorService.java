package com.lendtech.elevator.service;

import com.lendtech.elevator.entity.Elevator;

import java.util.List;

public interface ElevatorService {
    boolean callElevator(Long elevatorID, int destinationFloor);

    Elevator getElevatorStatusById(Long id);

    List<Elevator> getAllElevators();

    Elevator saveElevator(Elevator elevator);

}

