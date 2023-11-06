package mx.com.ferbo.facturacion.facturama.response;

import com.google.gson.annotations.SerializedName;

public class IdContabilidad {
    
    @SerializedName(value = "IdContabilidad")
    public String idContabilidad = null;

    public String getIdContabilidad() {
        return idContabilidad;
    }

    public void setIdContabilidad(String idContabilidad) {
        this.idContabilidad = idContabilidad;
    }

    @Override
    public String toString() {
        return "{\"idContabilidad\":\"" + idContabilidad + "\"}";
    }
}
