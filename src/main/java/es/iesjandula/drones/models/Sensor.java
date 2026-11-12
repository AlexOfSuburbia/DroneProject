package es.iesjandula.drones.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Sensor extends Base
{
    @Column
    private String type;

    @Column
    private String manufacturer;

    @ManyToOne
    private Drone droneId;
}
