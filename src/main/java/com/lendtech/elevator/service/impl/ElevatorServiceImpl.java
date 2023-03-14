package com.lendtech.elevator.service.impl;

import com.lendtech.elevator.entity.Elevator;
import com.lendtech.elevator.entity.ElevatorDirection;
import com.lendtech.elevator.entity.ElevatorDoorState;
import com.lendtech.elevator.repository.ElevatorRepository;
import com.lendtech.elevator.service.ElevatorService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

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
            log.info("-----------------------[ELEVATOR HASN'T BEEN CREATED YET]---------------------\n");
            return false;
        }

        if (destinationFloor > elevator.getMaxFloor()) {
            log.info("-----------------------[THAT FLOOR DOESNT EXIST IN THIS BUILDING]---------------------\n");
            return false;
        }

        if (destinationFloor == elevator.getCurrentFloor()){
            openDoor(elevator);
        }

        elevator.setDestinationFloor(destinationFloor);

        if (destinationFloor>elevator.getCurrentFloor()){
            log.info("-----------------------[ELEVATOR DIRECTION UP]---------------------\n");
            elevator.setDirection(ElevatorDirection.UP);
        }else {
            log.info("-----------------------[ELEVATOR DIRECTION DOWN]---------------------\n");
            elevator.setDirection(ElevatorDirection.DOWN);
        }

        moveElevator(elevator,destinationFloor);

        return true;
    }

    @Async
    public void moveElevator(Elevator elevator, int destination) throws InterruptedException {
        log.info("-----------------------[ELEVATOR {} IS MOVING FROM FLOOR {} TO FLOOR {}]---------------------\n",
                elevator.getId(), elevator.getCurrentFloor(),destination);

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
        log.info("-----------------------[ELEVATOR {} IS NOW AT FLOOR {}]---------------------\n",elevator.getId(), currentFloor);
        elevator.setCurrentFloor(currentFloor);
        elevatorRepository.save(elevator);

        if(currentFloor == destination){
            log.info("-----------------------[ELEVATOR {} HAS ARRIVED AT THE DESTINATION, OPENING THE DOOR]---------------------\n"
                    ,elevator.getId());
            elevator.setMoving(false);
            elevator.setDirection(ElevatorDirection.STOPPED);
            elevatorRepository.save(elevator);
            openDoor(elevator);
        }
    }
    @Async
    public void openDoor(Elevator elevator) throws InterruptedException {
        log.info("-----------------------[ELEVATOR {}'S DOOR IS OPENING]---------------------\n",elevator.getId());
        elevator.setElevatorDoorState(ElevatorDoorState.OPENING);
        elevatorRepository.save(elevator);

        Thread.sleep(2000);
        log.info("-----------------------[ELEVATOR {}'S DOOR IS OPEN]---------------------\n",elevator.getId());
        elevator.setElevatorDoorState(ElevatorDoorState.OPEN);
        elevatorRepository.save(elevator);

        log.info("-----------------------[ELEVATOR {}'S DOOR IS CLOSING]---------------------\n",elevator.getId());
        Thread.sleep(2000);
        elevator.setElevatorDoorState(ElevatorDoorState.CLOSING);
        elevatorRepository.save(elevator);

        Thread.sleep(2000);
        log.info("-----------------------[ELEVATOR {}'S DOOR IS CLOSED]---------------------\n",elevator.getId());
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
