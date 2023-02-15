package com.challenge.elevator.service.impl;

import com.challenge.elevator.entity.Elevator;
import com.challenge.elevator.entity.ElevatorDirection;
import com.challenge.elevator.entity.ElevatorDoorState;
import com.challenge.elevator.repository.ElevatorRepository;
import com.challenge.elevator.service.ElevatorService;
import com.challenge.elevator.util.NumberIterator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ElevatorServiceImpl implements ElevatorService {

    @Autowired
    ElevatorRepository elevatorRepository;

    @SneakyThrows
    @Override
    public boolean callElevator(Long elevatorID, int destinationFloor) {
        Elevator elevator = getElevatorStatusById(elevatorID);
        if (elevator == null) {
            log.info("Elevator hasn't been created yet");
            return false;
        }
        if (destinationFloor > elevator.getMaxFloor()) {
            log.info("That floor doesnt exist in this building");
            return false;
        }

        if (destinationFloor == elevator.getCurrentFloor()){
            openDoor(elevator);
        }

        elevator.setDestinationFloor(destinationFloor);
        if (destinationFloor>elevator.getCurrentFloor()){
            elevator.setDirection(ElevatorDirection.UP);
        }else {
            elevator.setDirection(ElevatorDirection.DOWN);
        }

        moveElevator(elevator,destinationFloor);

        return true;
    }

    @Async
    public void moveElevator(Elevator elevator, int destination) throws InterruptedException {
        log.info("Elevator {} Moving from Floor {} to Floor {}",elevator.getId(), elevator.getCurrentFloor(),destination);

        if (elevator.getDirection().equals(ElevatorDirection.UP)){
            for (int i = elevator.getCurrentFloor(); i <= destination; i++) {
                updateElevatorCurrentState(elevator, i, destination);
            }
        }

        if (elevator.getDirection().equals(ElevatorDirection.DOWN)){
            for (int i = elevator.getCurrentFloor(); i >= destination; i--) {
                updateElevatorCurrentState(elevator, i, destination);
            }
        }

    }

    private void updateElevatorCurrentState(Elevator elevator, int currentFloor,int destination) throws InterruptedException{
        elevator.setMoving(true);
        elevatorRepository.save(elevator);
        Thread.sleep(5000);
        log.info("The elevator {} is now at Floor {}",elevator.getId(), currentFloor);
        elevator.setCurrentFloor(currentFloor);
        elevatorRepository.save(elevator);

        if(currentFloor == destination){
            log.info("The elevator {} has arrived at the destination, opening the door", elevator.getId());
            elevator.setMoving(false);
            elevator.setDirection(ElevatorDirection.STOPPED);
            elevatorRepository.save(elevator);
            openDoor(elevator);
        }

    }
    @Async
    public void openDoor(Elevator elevator) throws InterruptedException {
        log.info("Elevator {}'s Door is opening",elevator.getId());
        elevator.setElevatorDoorState(ElevatorDoorState.OPENING);
        elevatorRepository.save(elevator);
        Thread.sleep(2000);
        log.info("Elevator {}'s Door open",elevator.getId());
        elevator.setElevatorDoorState(ElevatorDoorState.OPEN);
        elevatorRepository.save(elevator);
        log.info("Elevator {}'s Door is closing",elevator.getId());
        Thread.sleep(2000);
        elevator.setElevatorDoorState(ElevatorDoorState.CLOSING);
        elevatorRepository.save(elevator);
        Thread.sleep(2000);
        log.info("Elevator {}'s Door closed",elevator.getId());
        elevator.setElevatorDoorState(ElevatorDoorState.CLOSED);
        elevatorRepository.save(elevator);
    }

    @Override
    public Elevator getElevatorStatusById(Long id) {
        return elevatorRepository.findElevatorById(id);
    }

    @Override
    public List<Elevator> getAllElevators() {
        return elevatorRepository.findAll();
    }

    @Override
    public Elevator saveElevator(Elevator elevator){
        return elevatorRepository.save(elevator);
    }

}
