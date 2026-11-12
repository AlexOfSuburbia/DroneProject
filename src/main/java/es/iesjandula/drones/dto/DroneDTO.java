package es.iesjandula.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneDTO extends BaseDTO
{
    private String model;

    private Double maxAltitude;

    private Double maxSpeed;

    public DroneDTO(Long id, String name, String model, Double maxAltitude, Double maxSpeed) {
        super(id, name);
        this.model = model;
        this.maxAltitude = maxAltitude;
        this.maxSpeed = maxSpeed;
    }
}
