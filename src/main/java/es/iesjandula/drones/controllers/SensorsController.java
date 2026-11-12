package es.iesjandula.drones.controllers;

import es.iesjandula.drones.dto.SensorDTO;
import es.iesjandula.drones.dto.SensorDataDTO;
import es.iesjandula.drones.models.Sensor;
import es.iesjandula.drones.repository.SensorRepository;
import es.iesjandula.drones.service.SensorService;
import es.iesjandula.drones.utils.DroneException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import es.iesjandula.drones.utils.Constants;

@RestController
@RequestMapping(value="/api/v1/sensors")
@Slf4j
public class SensorsController
{
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private SensorService sensorService;

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> createSensor(@RequestBody SensorDataDTO sensorDto)
    {
        try
        {
            // Llamamos al servicio para crear el sensor
            SensorDTO creado = this.sensorService.crearSensor(sensorDto);

            // Devolvemos en un 200 el SensorDTO creado
            return ResponseEntity.ok().body(creado);
        }
        catch (DroneException droneException)
        {
            // Devolvemos un 400 (bad request) en caso de un error de validación
            // Además, el body de la respuesta es un JSON con el código, mensaje y stackTrace
            return ResponseEntity.status(400).body(droneException.getBodyExceptionMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se creaba el sensor";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<?> getAllSensors()
    {
        try
        {
            return ResponseEntity.ok().body(this.sensorRepository.buscarTodosSensores());
        }

        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se mostraba todos los sensores";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @RequestMapping(value="/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getSensor(@PathVariable Long id)
    {
        try
        {
            Optional<Sensor> sensor = this.sensorRepository.findById(id);
            if(sensor.isEmpty())
            {
                String errorString = "Id incorrecto";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_SENSOR_NO_EXISTE, errorString);
            }
            return ResponseEntity.ok().body(this.sensorRepository.buscarSensorPorId(id));
        }
        catch (DroneException e)
        {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se mostraba el sensor por id";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }

    @PostMapping(value = "/csv", consumes = "text/csv")
    public ResponseEntity<?> importSensorsCsv(@RequestBody String csv)
    {
        try
        {
            List<SensorDTO> sensores = this.sensorService.importarCsv(csv);
            return ResponseEntity.ok().body(sensores);
        }
        catch (DroneException e)
        {
            return ResponseEntity.status(404).body(e.getMessage());
        }
        catch (Exception exception)
        {
            //ERROR QUE NOSOTROS NO CONTROLAMOS (SE HA CAIDO LA BBDD, EL SERVIDOR...)

            // Creamos un mensaje de error
            String errorString = "Error mientras se importaba el sensor por CSV";

            // Error no controlado. Primero creamos una instancia de nuestra excepción personalizada
            DroneException droneException = new DroneException(Constants.ERROR_GENERICO, errorString, exception);

            // Logueamos
            log.error(errorString, droneException);

            // Devolvemos un 500
            return ResponseEntity.status(500).body(droneException.getBodyExceptionMessage());
        }
    }
}
