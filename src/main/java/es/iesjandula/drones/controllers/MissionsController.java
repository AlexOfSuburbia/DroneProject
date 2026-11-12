package es.iesjandula.drones.controllers;

import es.iesjandula.drones.dto.*;
import es.iesjandula.drones.models.Mission;
import es.iesjandula.drones.repository.MissionRepository;
import es.iesjandula.drones.service.MissionService;
import es.iesjandula.drones.utils.DroneException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import es.iesjandula.drones.utils.Constants;

@RestController
@RequestMapping(value="/api/v1/missions")
@Slf4j
public class MissionsController
{
    @Autowired
    private MissionRepository missionRepository;

    @Autowired
    private MissionService missionService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createMission(@RequestBody MissionDataDTO missionDto)
    {
        try
        {
            //Creamos una mission a través del service con el repositorio que hemos hecho para mission
            MissionDTO creado = this.missionService.createMission(missionDto);
            return ResponseEntity.ok().body(creado);
        }
        catch (DroneException e)
        {
            //Mostramos en consola los errores que hemos comprobado y recogido
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se creaba la mision";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllMissions()
    {
        try
        {
            //A traves del metodo creado en el servicio de mission, buscamos todas las misiones
            return ResponseEntity.ok().body(this.missionRepository.buscarTodasMisiones());
        }

        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se mostraba todas las missions";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMission(@PathVariable Long id)
    {
        try
        {
            //Nos aseguramos con el Optional que existe el dron
            Optional<Mission> mission = this.missionRepository.findById(id);
            if(mission.isEmpty())
            {
                String errorString = "Id incorrecto";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_MISSION_NO_EXISTE, errorString);
            }
            //Con el Query del repositorio buscamos las mission por id
            return ResponseEntity.ok().body(this.missionRepository.buscarMissionPorId(id));
        }
        catch (DroneException e)
        {
            //Recogemos los errores que nosootros hemos comprobado
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se mostraba el mission por ID";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @PostMapping(value = "/csv", consumes = "text/csv")
    public ResponseEntity<?> importMissionsCsv(@RequestBody String csv)
    {
        try
        {
            //A través del service de mission importamos en CSV
            List<MissionDTO> missions = this.missionService.importarCsv(csv);
            return ResponseEntity.ok().body(missions);
        }
        catch (DroneException e)
        {
            //Mostramos los errores que nosotros hemos comprobado
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se importaba el mission por CSV";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }
}
