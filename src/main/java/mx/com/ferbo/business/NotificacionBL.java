package mx.com.ferbo.business;

import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.validation.ValidationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mx.com.ferbo.dto.NotificacionDTO;
import mx.com.ferbo.util.SistemaOrigen;
import mx.com.ferbo.util.StatusNotificacion;
import mx.com.ferbo.util.TipoNotificacion;

@Named 
@RequestScoped 
public class NotificacionBL {
    
    public static final Logger log = LogManager.getLogger(NotificacionBL.class);

    public NotificacionDTO obtenerRespuesta(TipoNotificacion tipo) {

        if (tipo == null) {
            throw new ValidationException("El tipo de notificación no puede ser vacía");
        }

        NotificacionDTO respuesta = new NotificacionDTO();
        respuesta.setSistemaOrigen(SistemaOrigen.GESTION_PF);
        respuesta.setStatus(StatusNotificacion.RECIVIDA);
        respuesta.setTipo(tipo);
        LocalDateTime cdmxDateTime = LocalDateTime.now(ZoneId.of("America/Mexico_City"));
        respuesta.setFecha(cdmxDateTime);

        return respuesta;
    }

}
