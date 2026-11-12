package es.iesjandula.drones.controllers;

import es.iesjandula.drones.dto.DroneDTO;
import es.iesjandula.drones.dto.DroneDataDTO;
import es.iesjandula.drones.models.Drone;
import es.iesjandula.drones.repository.DroneRepository;
import es.iesjandula.drones.service.DroneService;
import es.iesjandula.drones.utils.DroneException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import es.iesjandula.drones.utils.Constants;

@RestController
@RequestMapping(value="/api/v1/drones")
@Slf4j
public class DronesController
{
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneService droneService;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public ResponseEntity<?> createDrone(@RequestBody DroneDataDTO dronDto)
    {
        try
        {
            //A través del servicio del drone, usamos el metodo creado para crear un dron
            DroneDTO creado = this.droneService.crearDrone(dronDto);
            return ResponseEntity.ok().body(creado);
        }
        catch (DroneException e)
        {
            //Recogemos cualquier error que nosotros hayamos comprobado
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se creaba el drone";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllDrones()
    {
        try
        {
            return ResponseEntity.ok().body(this.droneRepository.buscarTodosDrones());
        }

        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se mostraba todos los drones";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getDrone(@PathVariable Long id)
    {
        try
        {
            //Buscamos con el Optional por si no existiese un dron con ese id
            Optional<Drone> drone = this.droneRepository.findById(id);
            if(drone.isEmpty())
            {
                String errorString = "Id incorrecto";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_DRONE_NO_EXISTE, errorString);
            }
            return ResponseEntity.ok().body(this.droneRepository.buscarDronePorId(id));
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se mostraba el drone por ID";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(value="/csv", method = RequestMethod.POST, consumes = "test/csv")
    public ResponseEntity<?> importDronesCsv(@RequestBody String csv)
    {
        try
        {
            //A través del servicio del drone, usamos el metodo creado para importarlo en CSV
            List<DroneDTO> drones = this.droneService.importarCsv(csv);
            return ResponseEntity.ok().body(drones);
        }
        catch (DroneException e)
        {
            //Recogemos en consola cualquier eerror que se haya comprobad y hemos recogido
            return ResponseEntity.status(400).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se importaba el drone por CSV";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }
}
