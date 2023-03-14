package com.lendtech.elevator.controller;

import com.lendtech.elevator.entity.Elevator;
import com.lendtech.elevator.entity.StringResponse;
import com.lendtech.elevator.service.ElevatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/elevator")
public class ElevatorController {

    @Value("${elevator.max.floor}")
    private int maxFloor;

    private ElevatorService elevatorService;

    @Autowired
    public ElevatorController(ElevatorService elevatorService) {
        this.elevatorService = elevatorService;
    }

    @PostMapping("/add-elevator")
    public ResponseEntity<StringResponse> addElevator() {
        Elevator elevator = new Elevator();
        elevator.setMaxFloor(maxFloor);
        Elevator created = elevatorService.saveElevator(elevator);
        if (null != created) {
            log.info("-----------------------[ADDING ELEVATOR SUCCESS]---------------------\n{}", created);
            return new ResponseEntity<>(new StringResponse("Elevator Added Successfully"), HttpStatus.OK);
        } else {
            log.info("-----------------------[ADDING ELEVATOR ERROR]---------------------\n");
            return new ResponseEntity<>(new StringResponse("Elevator could not be added."),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/call")
    public ResponseEntity<StringResponse> callElevator(@RequestParam("destinationFloor") int destinationFloor,
                                               @RequestParam("elevatorID") Long elevatorID) {
        boolean success = elevatorService.callElevator(elevatorID, destinationFloor);
        if (success) {
            log.info("-----------------------[ELEVATOR CALLED SUCCESSFULLY]---------------------\n");
            return new ResponseEntity<>(new StringResponse("Elevator Called Successfully"), HttpStatus.OK);
        } else {
            log.info("-----------------------[ELEVATOR COULD NOT BE CALLED]---------------------\n");
            return new ResponseEntity<>(new StringResponse("Elevator could not be called"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Elevator>> getElevatorStatus() {
        List<Elevator> elevator = elevatorService.getAllElevators();
        if (!elevator.isEmpty()) {
            log.info("-----------------------[ELEVATOR STATUS]---------------------\n{}", elevator);
            return ResponseEntity.ok(elevator);
        } else {
            log.info("-----------------------[ELEVATOR STATUS NOT FOUND]---------------------\n{}", elevator);
            return ResponseEntity.notFound().build();
        }
    }
}
