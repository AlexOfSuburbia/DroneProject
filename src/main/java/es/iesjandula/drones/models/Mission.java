package es.iesjandula.drones.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Mission extends Base
{
    //Entidad que representa una misión de vuelo asignada a un dron.
    @Column
    private String description;

    @Column
    private Double maxAltitude;

    @ManyToOne
    private Drone droneId;
}
