package mx.com.ferbo.dto;

import java.time.LocalDateTime;

import mx.com.ferbo.util.SistemaOrigen;
import mx.com.ferbo.util.StatusNotificacion;
import mx.com.ferbo.util.TipoNotificacion;

public class NotificacionDTO {

    private SistemaOrigen sistemaOrigen;
    private  TipoNotificacion tipo;
    private StatusNotificacion status;
    private LocalDateTime fecha;

    public SistemaOrigen getSistemaOrigen() {
        return sistemaOrigen;
    }

    public void setSistemaOrigen(SistemaOrigen sistemaOrigen) {
        this.sistemaOrigen = sistemaOrigen;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }
    
    public void setTipo(TipoNotificacion tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public StatusNotificacion getStatus() {
        return status;
    }

    public void setStatus(StatusNotificacion status) {
        this.status = status;
    }

    

}
