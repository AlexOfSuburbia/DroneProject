package es.iesjandula.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MissionDataDTO
{
    private String name;

    private String description;

    private Double maxAltitude;

    private Long droneId;
}
