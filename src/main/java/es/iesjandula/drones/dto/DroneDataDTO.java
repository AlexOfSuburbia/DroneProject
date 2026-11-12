package es.iesjandula.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DroneDataDTO
{
    private String name;

    private String model;

    private Double maxAltitude;

    private Double maxSpeed;
}
