package es.iesjandula.drones.service;

import es.iesjandula.drones.dto.DroneDTO;
import es.iesjandula.drones.dto.DroneDataDTO;
import es.iesjandula.drones.models.Drone;
import es.iesjandula.drones.repository.DroneRepository;
import es.iesjandula.drones.utils.DroneException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import es.iesjandula.drones.utils.Constants;

@Service
@Slf4j
public class DroneService
{
    @Autowired
    private DroneRepository droneRepository;

    public DroneDTO crearDrone(DroneDataDTO dronDto) throws DroneException
    {
        //ERROR QUE NOSOTROS SI CONTROLAMOS
        if(dronDto.getModel() == null || dronDto.getModel().isEmpty())
        {
            String errorString = "Datos incorrectos";

            //Log
            log.error(errorString);

            //Lanzar exception
            throw new DroneException(Constants.ERROR_CREANDO_DRONE, errorString);
        }
        Drone dron = new Drone();

        dron.setName(dronDto.getName());
        dron.setModel(dronDto.getModel());
        dron.setMaxAltitude(dronDto.getMaxAltitude());
        dron.setMaxSpeed(dronDto.getMaxSpeed());

        Drone guardado = this.droneRepository.save(dron);
        return new DroneDTO(guardado.getId(), guardado.getName(), guardado.getModel(),  guardado.getMaxAltitude(), guardado.getMaxSpeed());
    }

    public List<DroneDTO> importarCsv(String csv) throws DroneException
    {
        List<Drone> entidades = new ArrayList<>();

        String[] lineas = csv.split("\\R");   // \R = cualquier salto de línea (Windows o Linux)

        for (int i = 0; i < lineas.length; i++)
        {
            String linea = lineas[i].trim();

            if (linea.isEmpty())
            {
                continue;
            }

            String[] campos = linea.split(",");

            if (campos.length != 4)
            {
                String errorString = "CSV incorrecto, varios campos innecesarios detectados";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_DRONE, errorString);
            }

            try
            {
                Drone drone = new Drone();
                drone.setName(campos[0].trim());
                drone.setModel(campos[1].trim());
                drone.setMaxAltitude(Double.parseDouble(campos[2].trim()));
                drone.setMaxSpeed(Double.parseDouble(campos[3].trim()));
                entidades.add(drone);
            }
            catch (Exception e)
            {
                String errorString = "Error al insertar CSV";

                //Log
                log.error(errorString);

                //Lanzar exception
                throw new DroneException(Constants.ERROR_CONTENIDO_CSV_DRONE, errorString);
            }
        }

        List<Drone> guardados = this.droneRepository.saveAll(entidades);

        // convertir a DTO de salida
        List<DroneDTO> resultado = new ArrayList<>();
        for (Drone d : guardados)
        {
            resultado.add(new DroneDTO(d.getId(), d.getName(), d.getModel(), d.getMaxAltitude(), d.getMaxSpeed()));
        }
        return resultado;
    }
}
