package mx.com.ferbo.facturacion.facturama.response;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Ine {
    
    @SerializedName(value = "Entidad")
    private List<IneEntidad> entidad = null;
    
    @SerializedName(value = "Version")
    private String version = null;
    
    @SerializedName(value = "TipoProceso")
    private String tipoProceso = null;
    
    @SerializedName(value = "TipoComite")
    private String tipoComite = null;
    
    @SerializedName(value = "TipoComiteSpecified")
    private Boolean tipoComiteSpecified = null;
    
    @SerializedName(value = "IdContabilidad")
    private String idContabilidad = null;
    
    @SerializedName(value = "IdContabilidadSpecified")
    private Boolean idContabilidadSpecified = null;

    public List<IneEntidad> getEntidad() {
        return entidad;
    }

    public void setEntidad(List<IneEntidad> entidad) {
        this.entidad = entidad;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getTipoProceso() {
        return tipoProceso;
    }

    public void setTipoProceso(String tipoProceso) {
        this.tipoProceso = tipoProceso;
    }

    public String getTipoComite() {
        return tipoComite;
    }

    public void setTipoComite(String tipoComite) {
        this.tipoComite = tipoComite;
    }

    public Boolean getTipoComiteSpecified() {
        return tipoComiteSpecified;
    }

    public void setTipoComiteSpecified(Boolean tipoComiteSpecified) {
        this.tipoComiteSpecified = tipoComiteSpecified;
    }

    public String getIdContabilidad() {
        return idContabilidad;
    }

    public void setIdContabilidad(String idContabilidad) {
        this.idContabilidad = idContabilidad;
    }

    public Boolean getIdContabilidadSpecified() {
        return idContabilidadSpecified;
    }

    public void setIdContabilidadSpecified(Boolean idContabilidadSpecified) {
        this.idContabilidadSpecified = idContabilidadSpecified;
    }

    @Override
    public String toString() {
        return "{\"entidad\":\"" + entidad + "\", \"version\":\"" + version + "\", \"tipoProceso\":\"" + tipoProceso
                + "\", \"tipoComite\":\"" + tipoComite + "\", \"tipoComiteSpecified\":\"" + tipoComiteSpecified
                + "\", \"idContabilidad\":\"" + idContabilidad + "\", \"idContabilidadSpecified\":\""
                + idContabilidadSpecified + "\"}";
    }
}
