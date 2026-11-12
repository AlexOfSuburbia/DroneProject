package es.iesjandula.drones.repository;

import es.iesjandula.drones.dto.DroneDTO;
import es.iesjandula.drones.dto.MissionDTO;
import es.iesjandula.drones.models.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MissionRepository extends JpaRepository<Mission, Long>
{
    @Query("""
                SELECT new es.iesjandula.drones.dto.MissionDTO(m.id, m.name, m.description, m.maxAltitude, m.droneId.id)
                FROM Mission m
           """)
    List<DroneDTO> buscarTodasMisiones();

    @Query("""
                SELECT new es.iesjandula.drones.dto.MissionDTO(m.id, m.name, m.description, m.maxAltitude, m.droneId.id)
                FROM Mission m
                WHERE m.id = :idData
            """)
    DroneDTO buscarMissionPorId(Long idData);
}
