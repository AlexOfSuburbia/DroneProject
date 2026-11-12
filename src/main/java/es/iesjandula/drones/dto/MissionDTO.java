package es.iesjandula.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MissionDTO extends BaseDTO
{
    private String description;

    private Double maxAltitude;

    private Long droneId;

    public MissionDTO(Long id, String name, String description, Double maxAltitude, Long droneId) {
        super(id, name);
        this.description = description;
        this.maxAltitude = maxAltitude;
        this.droneId = droneId;
    }
}
