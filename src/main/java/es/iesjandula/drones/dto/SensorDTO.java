package es.iesjandula.drones.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SensorDTO extends BaseDTO
{
    private String type;

    private String manufacturer;

    private Long droneId;

    public SensorDTO(Long id, String name, String type, String manufacturer, Long droneId) {
        super(id, name);
        this.type = type;
        this.manufacturer = manufacturer;
        this.droneId = droneId;
    }
}
