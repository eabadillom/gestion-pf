package mx.com.ferbo.dto;

import java.io.Serializable;
import mx.com.ferbo.model.ClienteDomicilios;

/**
 *
 * @author alberto
 */
public class ClientesDomiciliosOperacion implements Serializable 
{
    private static final long serialVersionUID = 1L;

    public enum EstadoOperacion {
        NUEVO,
        EXISTENTE,
        ACTUALIZADO,
        ELIMINADO_TEMP
    }

    private ClienteDomicilios clienteDomicilios;
    private EstadoOperacion estado;

    public ClientesDomiciliosOperacion(ClienteDomicilios clienteDomicilios, EstadoOperacion estado) {
        this.clienteDomicilios = clienteDomicilios;
        this.estado = estado;
    }

    public ClienteDomicilios getClienteDomicilios() {
        return clienteDomicilios;
    }

    public void setClienteDomicilios(ClienteDomicilios clienteDomicilios) {
        this.clienteDomicilios = clienteDomicilios;
    }

    public EstadoOperacion getEstado() {
        return estado;
    }

    public void setEstado(EstadoOperacion estado) {
        this.estado = estado;
    }

}
