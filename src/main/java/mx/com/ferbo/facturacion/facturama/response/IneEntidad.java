package mx.com.ferbo.facturacion.facturama.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class IneEntidad {
    
    @SerializedName(value = "Contabilidad")
    private List<IdContabilidad> contabilidad = null;
    
    @SerializedName(value = "ClaveEntidad")
    private String claveEntidad = null;
    
    @SerializedName(value = "Ambito")
    private String ambito = null;
    
    @SerializedName(value = "AmbitoSpecified")
    private Boolean ambitoSpecified = null;

    public List<IdContabilidad> getContabilidad() {
        return contabilidad;
    }

    public void setContabilidad(List<IdContabilidad> contabilidad) {
        this.contabilidad = contabilidad;
    }

    public String getClaveEntidad() {
        return claveEntidad;
    }

    public void setClaveEntidad(String claveEntidad) {
        this.claveEntidad = claveEntidad;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public Boolean getAmbitoSpecified() {
        return ambitoSpecified;
    }

    public void setAmbitoSpecified(Boolean ambitoSpecified) {
        this.ambitoSpecified = ambitoSpecified;
    }

    @Override
    public String toString() {
        return "{\"contabilidad\":\"" + contabilidad + "\", \"claveEntidad\":\"" + claveEntidad + "\", \"ambito\":\""
                + ambito + "\", \"ambitoSpecified\":\"" + ambitoSpecified + "\"}";
    }
}
