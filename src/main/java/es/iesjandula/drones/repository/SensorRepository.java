package es.iesjandula.drones.repository;

import es.iesjandula.drones.dto.DroneDTO;
import es.iesjandula.drones.dto.SensorDTO;
import es.iesjandula.drones.models.Sensor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SensorRepository extends JpaRepository<Sensor, Long>
{
    @Query("""
                SELECT new es.iesjandula.drones.dto.SensorDTO(s.id, s.name, s.type, s.manufacturer, s.droneId.id)
                FROM Sensor s
           """)
    List<DroneDTO> buscarTodosSensores();

    @Query("""
                SELECT new es.iesjandula.drones.dto.SensorDTO(s.id, s.name, s.type, s.manufacturer, s.droneId.id)
                FROM Sensor s
                WHERE s.id = :idData
            """)
    DroneDTO buscarSensorPorId(Long idData);
}
