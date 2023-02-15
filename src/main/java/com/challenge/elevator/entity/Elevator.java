package com.challenge.elevator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Data
@ToString
@Entity
@Table(name = "elevator")
public class Elevator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int currentFloor;
    private int destinationFloor;
    private ElevatorDirection direction;
    private boolean isMoving;
    private ElevatorDoorState elevatorDoorState;
    private int maxFloor;

    public Elevator() {
        this.currentFloor = 1;
        this.destinationFloor = 1;
        this.direction = ElevatorDirection.STOPPED;
        this.isMoving = false;
        this.elevatorDoorState = ElevatorDoorState.CLOSED;
    }
}
