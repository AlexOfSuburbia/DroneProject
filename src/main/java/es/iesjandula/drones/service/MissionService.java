package es.iesjandula.drones.service;

import es.iesjandula.drones.dto.MissionDTO;
import es.iesjandula.drones.dto.MissionDataDTO;
import es.iesjandula.drones.models.Drone;
import es.iesjandula.drones.models.Mission;
import es.iesjandula.drones.repository.DroneRepository;
import es.iesjandula.drones.repository.MissionRepository;
import es.iesjandula.drones.utils.DroneException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import es.iesjandula.drones.utils.Constants;

@Service
@Slf4j
public class MissionService
{
    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private DroneRepository droneRepository;

    public MissionDTO createMission(MissionDataDTO missionDto) throws DroneException
    {
        if(missionDto.getDroneId() == null)
        {
            String errorString = "Datos incorrectos";

            //Log
            log.error(errorString);

            //Lanzar exception
            throw new DroneException(Constants.ERROR_CREANDO_MISSION, errorString);
        }
        Mission mission = new Mission();
        mission.setId(missionDto.getDroneId());
        mission.setName(missionDto.getName());
        mission.getDescription();
        mission.getMaxAltitude();
        mission.getDroneId();

        Mission guardado = this.missionRepository.save(mission);
        return new MissionDTO(mission.getId(), mission.getName(), guardado.getDescription(), guardado.getMaxAltitude(), guardado.getId());
    }

    public List<MissionDTO> importarCsv(String csv) throws DroneException
    {
        List<Mission> entidades = new ArrayList<>();

        String[] lineas = csv.split("\\R");

        for (int i = 0; i < lineas.length; i++)
        {
            String linea = lineas[i].trim();

            if (linea.isEmpty())
            {
                continue;
            }

            String[] campos = linea.split(",", -1);

            if (campos.length != 4)
            {
                String errorString = "CSV incorrecto, varios campos innecesarios detectados";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_MISSION, errorString);
            }

            String nombre = campos[0].trim();
            String descripcion = campos[1].trim();

            if (nombre.isEmpty())
            {
                String errorString = "Error al insertar CSV";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_MISSION, errorString);
            }

            // maxAltitude: texto -> Double
            Double maxAltitude;
            try
            {
                maxAltitude = Double.parseDouble(campos[2].trim());
            }
            catch (NumberFormatException e)
            {
                String errorString = "Error al insertar maxAltitude";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_MISSION, errorString);
            }

            if (maxAltitude <= 0)
            {
                String errorString = "maxAltitude debe ser mayor a 0";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_MISSION, errorString);
            }

            // droneId: texto -> Long
            Long droneId;
            try
            {
                droneId = Long.parseLong(campos[3].trim());
            }
            catch (NumberFormatException e)
            {
                String errorString = "Error al insertar droneId";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_MISSION, errorString);
            }

            // buscar el dron (404 si no existe)
            Optional<Drone> drone = this.droneRepository.findById(droneId);
            if (drone.isEmpty())
            {
                String errorString = "No existe dron con ese id";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_MISSION, errorString);
            }

            Mission mission = new Mission();
            mission.setName(nombre);
            mission.setDescription(descripcion);
            mission.setMaxAltitude(maxAltitude);
            mission.setDroneId(drone.get());
            entidades.add(mission);
        }

        List<Mission> guardadas = this.missionRepository.saveAll(entidades);

        // convertir a DTO de salida
        List<MissionDTO> resultado = new ArrayList<>();
        for (Mission m : guardadas)
        {
            MissionDTO dto = new MissionDTO();
            dto.setDescription(m.getDescription());
            dto.setMaxAltitude(m.getMaxAltitude());
            dto.setDroneId(m.getId());
            resultado.add(dto);
        }
        return resultado;
    }
}
