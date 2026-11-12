package es.iesjandula.drones.service;

import es.iesjandula.drones.dto.SensorDTO;
import es.iesjandula.drones.dto.SensorDataDTO;
import es.iesjandula.drones.models.Drone;
import es.iesjandula.drones.models.Sensor;
import es.iesjandula.drones.repository.DroneRepository;
import es.iesjandula.drones.repository.SensorRepository;
import es.iesjandula.drones.utils.DroneException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import es.iesjandula.drones.utils.Constants;

@Service
@Slf4j
public class SensorService
{
    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private DroneRepository droneRepository;

    public SensorDTO crearSensor(SensorDataDTO sensorDto) throws DroneException
    {
        if(sensorDto == null || sensorDto.getDroneId() == null)
        {
            String errorString = "Datos incorrectos";

            //Log
            log.error(errorString);

            //Lanzar exception
            throw new DroneException(Constants.ERROR_CREANDO_SENSOR, errorString);
        }
        Sensor sensor = new Sensor();
        sensor.setName(sensorDto.getName());
        sensor.setType(sensorDto.getType());
        sensor.setManufacturer(sensorDto.getManufacturer());
        sensor.setId(sensorDto.getDroneId());

        Sensor guardado = this.sensorRepository.save(sensor);
        return new SensorDTO(guardado.getId(), guardado.getName(), guardado.getType(), guardado.getManufacturer(), guardado.getId());
    }

    public List<SensorDTO> importarCsv(String csv) throws DroneException
    {
        List<Sensor> entidades = new ArrayList<>();

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
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_SENSOR, errorString);
            }

            String nombre = campos[0].trim();
            String tipo = campos[1].trim().toUpperCase();
            String fabricante = campos[2].trim();

            if (nombre.isEmpty())
            {
                String errorString = "nombre vacío";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_SENSOR, errorString);
            }

            // droneId: texto -> Long
            Long droneId;
            try
            {
                droneId = Long.parseLong(campos[3].trim());
            }
            catch (NumberFormatException e)
            {
                String errorString = "error al insertar droneId";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_SENSOR, errorString);
            }

            // buscar el dron (404 si no existe)
            Optional<Drone> drone = this.droneRepository.findById(droneId);
            if (drone.isEmpty())
            {
                String errorString = "No existe un dron con ese id";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_SENSOR, errorString);
            }

            Sensor sensor = new Sensor();
            sensor.setName(nombre);
            sensor.setType(tipo);
            sensor.setManufacturer(fabricante);
            sensor.setId(droneId);
            entidades.add(sensor);
        }

        List<Sensor> guardados = this.sensorRepository.saveAll(entidades);

        List<SensorDTO> resultado = new ArrayList<>();
        for (Sensor s : guardados)
        {
            SensorDTO dto = new SensorDTO();
            dto.setType(s.getType());
            dto.setManufacturer(s.getManufacturer());
            dto.setDroneId(s.getId());
            resultado.add(dto);
        }
        return resultado;
    }
}
