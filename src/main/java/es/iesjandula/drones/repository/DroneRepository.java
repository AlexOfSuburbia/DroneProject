package es.iesjandula.drones.repository;

import es.iesjandula.drones.dto.DroneDTO;
import es.iesjandula.drones.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long>
{
    @Query("""
                SELECT new es.iesjandula.drones.dto.DroneDTO(d.id, d.name, d.model, d.maxAltitude, d.maxSpeed)
                FROM Drone d
           """)
    List<DroneDTO> buscarTodosDrones();


    @Query("""
                SELECT new es.iesjandula.drones.dto.DroneDTO(d.id, d.name, d.model, d.maxAltitude, d.maxSpeed)
                FROM Drone d
                WHERE d.id = :idData
            """)
    DroneDTO buscarDronePorId(Long idData);
}
