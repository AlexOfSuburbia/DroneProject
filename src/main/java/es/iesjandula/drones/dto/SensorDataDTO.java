package es.iesjandula.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SensorDataDTO
{
    private String name;

    private String type;

    private String manufacturer;

    private Long droneId;
}
