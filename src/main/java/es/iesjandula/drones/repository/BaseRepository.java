package es.iesjandula.drones.repository;

import es.iesjandula.drones.dto.MissionDataDTO;
import es.iesjandula.drones.models.Base;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BaseRepository extends JpaRepository<Base, Long>
{
}
