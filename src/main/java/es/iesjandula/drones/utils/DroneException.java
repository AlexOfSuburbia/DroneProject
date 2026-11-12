package es.iesjandula.drones.utils;



import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.HashMap;
import java.util.Map;

public class DroneException extends Exception {
    private String codigo;

    public DroneException(String codigo, String message)
    {
        super(message);
        this.codigo = codigo;
    }

    /**
     * Constructor con código, mensaje y causa original.
     */
    public DroneException(String errorId, String message, Throwable excepcion)
    {
        super(message, excepcion);
        this.codigo = errorId;
    }

    /**
     * Devuelve un mapa con los detalles de la excepción:
     * código, mensaje y stacktrace de la causa si existe.
     */
    public Object getBodyExceptionMessage()
    {
        Map<String, Object> mapBodyException = new HashMap<>();
        mapBodyException.put("codigo", this.codigo);
        mapBodyException.put("message", this.getMessage());

        if (this.getCause() != null)
        {
            String stackTrace = ExceptionUtils.getStackTrace(this.getCause());
            mapBodyException.put("excepcion", stackTrace);
        }

        return mapBodyException;
    }
}
