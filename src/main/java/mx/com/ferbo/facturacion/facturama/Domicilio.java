package mx.com.ferbo.facturacion.facturama;

import com.google.gson.annotations.SerializedName;

/**Domicilio<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Domicilio<br>
 * @author esteban
 *
 */
public class Domicilio {

    @SerializedName(value = "Calle")
    private String calle = null;
    
    @SerializedName(value = "NumeroExterior")
    private String numeroExterior = null;
    
    @SerializedName(value = "NumeroInterior")
    private String numeroInterior = null;
    
    @SerializedName(value = "Colonia")
    private String colonia = null;
    
    @SerializedName(value = "Localidad")
    private String localidad = null;
    
    @SerializedName(value = "Referencia")
    private String referencia = null;
    
    @SerializedName(value = "Municipio")
    private String municipio = null;
    
    @SerializedName(value = "MunicipioName")
    private String municipioName = null;
    
    /**Estado<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,30}<br>
     */
    @SerializedName(value = "Estado")
    private String estado = null;
    
    /**Pais<br>
     * Required<br>
     */
    @SerializedName(value = "Pais")
    private String pais = null;
    
    /**CodigoPostal<br>
     * Required<br>
     */
    @SerializedName(value = "CodigoPostal")
    private String codigoPostal = null;

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumeroExterior() {
        return numeroExterior;
    }

    public void setNumeroExterior(String numeroExterior) {
        this.numeroExterior = numeroExterior;
    }

    public String getNumeroInterior() {
        return numeroInterior;
    }

    public void setNumeroInterior(String numeroInterior) {
        this.numeroInterior = numeroInterior;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getMunicipioName() {
        return municipioName;
    }

    public void setMunicipioName(String municipioName) {
        this.municipioName = municipioName;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    @Override
    public String toString() {
        return "{\"calle\":\"" + calle + "\", \"numeroExterior\":\"" + numeroExterior + "\", \"numeroInterior\":\""
                + numeroInterior + "\", \"colonia\":\"" + colonia + "\", \"localidad\":\"" + localidad
                + "\", \"referencia\":\"" + referencia + "\", \"municipio\":\"" + municipio + "\", \"municipioName\":\""
                + municipioName + "\", \"estado\":\"" + estado + "\", \"pais\":\"" + pais + "\", \"codigoPostal\":\""
                + codigoPostal + "\"}";
    }
    
}
