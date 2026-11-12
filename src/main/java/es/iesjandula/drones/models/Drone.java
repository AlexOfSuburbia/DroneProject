package es.iesjandula.drones.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Drone extends Base
{
    //Entidad que representa un dron registrado en el sistema.
    @Column
    private String model;

    @Column
    private Double maxAltitude;

    @Column
    private Double maxSpeed;

    @OneToMany(mappedBy = "droneId")
    private List<Sensor> sensors;

    @OneToMany(mappedBy = "droneId")
    private List<Mission> missions;
}
