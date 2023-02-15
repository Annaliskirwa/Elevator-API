package com.challenge.elevator.controller;

import com.challenge.elevator.entity.Elevator;
import com.challenge.elevator.entity.StringResponse;
import com.challenge.elevator.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<StringResponse> addElevator() {
        Elevator elevator = new Elevator();
        elevator.setMaxFloor(maxFloor);
        Elevator created = elevatorService.saveElevator(elevator);
        if (null != created) {
            return new ResponseEntity<>(new StringResponse("Elevator Added Successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StringResponse("Elevator could not be added."),HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/call")
    public ResponseEntity<StringResponse> callElevator(@RequestParam("destinationFloor") int destinationFloor,
                                               @RequestParam("elevatorID") Long elevatorID) {
        boolean success = elevatorService.callElevator(elevatorID, destinationFloor);
        if (success) {
            return new ResponseEntity<>(new StringResponse("Elevator Called Successfully"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new StringResponse("Elevator could not be called"),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Elevator>> getElevatorStatus() {
        List<Elevator> elevator = elevatorService.getAllElevators();
        if (!elevator.isEmpty()) {
            return ResponseEntity.ok(elevator);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
