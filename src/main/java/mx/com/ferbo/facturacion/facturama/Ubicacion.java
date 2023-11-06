package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;


/**Ubicacion<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=Ubicacion<br>
 * @author esteban
 *
 */
public class Ubicacion {
    
    /**Id de facturama
     */
    @SerializedName(value = "Id")
    private String id = null;
    
    /**Domicilio
     */
    @SerializedName(value = "Domicilio")
    private Domicilio domicilio = null;
    
    /**TipoUbicacion<br>
     * Required<br>
     */
    @SerializedName(value = "TipoUbicacion")
    private String tipoUbicacion = null;
    
    /**IDUbicacion<br>
     * Matching regular expression pattern: (OR|DE)[0-9]{6}
     */
    @SerializedName(value = "IDUbicacion")
    private String idUbicacion = null;
    
    /**RFCRemitenteDestinatario<br>
     * Required<br>
     * Matching regular expression pattern: [A-Z&Ñ]{3,4}[0-9]{2}(0[1-9]|1[012])(0[1-9]|[12][0-9]|3[01])[A-Z0-9]{2}[0-9A]<br>
     */
    @SerializedName(value = "RFCRemitenteDestinatario")
    private String rfcRemitenteDestinatario = null;
    
    /**NombreRemitenteDestinatario<br>
     * Matching regular expression pattern: [^|]{1,254}<br>
     */
    @SerializedName(value = "NombreRemitenteDestinatario")
    private String nombreRemitenteDestinatario = null;
    
    /**NumRegIdTrib<br>
     * Matching regular expression pattern: [^|]{6,40}<br>
     */
    @SerializedName(value = "NumRegIdTrib")
    private String numRegIdTrib = null;
    
    @SerializedName(value = "ResidenciaFiscal")
    private String residenciaFiscal = null;
    
    @SerializedName(value = "NumEstacion")
    private String numEstacion = null;
    
    /**NombreEstacion<br>
     * Matching regular expression pattern: [^|]{1,50}<br>
     */
    @SerializedName(value = "NombreEstacion")
    private String nombreEstacion = null;
    
    /**NavegacionTrafico<br>
     */
    @SerializedName(value = "NavegacionTrafico")
    private String navegacionTrafico = null;
    
    /**FechaHoraSalidaLlegada<br>
     * Required<br>
     * Data type: Text<br>
     */
    @SerializedName(value = "FechaHoraSalidaLlegada")
    private String fechaHoraSalidaLlegada = null;
    
    @SerializedName(value = "TipoEstacion")
    private String TipoEstacion = null;
    
    @SerializedName(value = "DistanciaRecorrida")
    private BigDecimal distanciaRecorrida = null;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }

    public String getTipoUbicacion() {
        return tipoUbicacion;
    }

    public void setTipoUbicacion(String tipoUbicacion) {
        this.tipoUbicacion = tipoUbicacion;
    }

    public String getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(String idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public String getRfcRemitenteDestinatario() {
        return rfcRemitenteDestinatario;
    }

    public void setRfcRemitenteDestinatario(String rfcRemitenteDestinatario) {
        this.rfcRemitenteDestinatario = rfcRemitenteDestinatario;
    }

    public String getNombreRemitenteDestinatario() {
        return nombreRemitenteDestinatario;
    }

    public void setNombreRemitenteDestinatario(String nombreRemitenteDestinatario) {
        this.nombreRemitenteDestinatario = nombreRemitenteDestinatario;
    }

    public String getNumRegIdTrib() {
        return numRegIdTrib;
    }

    public void setNumRegIdTrib(String numRegIdTrib) {
        this.numRegIdTrib = numRegIdTrib;
    }

    public String getResidenciaFiscal() {
        return residenciaFiscal;
    }

    public void setResidenciaFiscal(String residenciaFiscal) {
        this.residenciaFiscal = residenciaFiscal;
    }

    public String getNumEstacion() {
        return numEstacion;
    }

    public void setNumEstacion(String numEstacion) {
        this.numEstacion = numEstacion;
    }

    public String getNombreEstacion() {
        return nombreEstacion;
    }

    public void setNombreEstacion(String nombreEstacion) {
        this.nombreEstacion = nombreEstacion;
    }

    public String getNavegacionTrafico() {
        return navegacionTrafico;
    }

    public void setNavegacionTrafico(String navegacionTrafico) {
        this.navegacionTrafico = navegacionTrafico;
    }

    public String getFechaHoraSalidaLlegada() {
        return fechaHoraSalidaLlegada;
    }

    public void setFechaHoraSalidaLlegada(String fechaHoraSalidaLlegada) {
        this.fechaHoraSalidaLlegada = fechaHoraSalidaLlegada;
    }

    public String getTipoEstacion() {
        return TipoEstacion;
    }

    public void setTipoEstacion(String tipoEstacion) {
        TipoEstacion = tipoEstacion;
    }

    public BigDecimal getDistanciaRecorrida() {
        return distanciaRecorrida;
    }

    public void setDistanciaRecorrida(BigDecimal distanciaRecorrida) {
        this.distanciaRecorrida = distanciaRecorrida;
    }

    @Override
    public String toString() {
        return "{\"id\":\"" + id + "\", \"domicilio\":\"" + domicilio + "\", \"tipoUbicacion\":\"" + tipoUbicacion
                + "\", \"idUbicacion\":\"" + idUbicacion + "\", \"rfcRemitenteDestinatario\":\""
                + rfcRemitenteDestinatario + "\", \"nombreRemitenteDestinatario\":\"" + nombreRemitenteDestinatario
                + "\", \"numRegIdTrib\":\"" + numRegIdTrib + "\", \"residenciaFiscal\":\"" + residenciaFiscal
                + "\", \"numEstacion\":\"" + numEstacion + "\", \"nombreEstacion\":\"" + nombreEstacion
                + "\", \"navegacionTrafico\":\"" + navegacionTrafico + "\", \"fechaHoraSalidaLlegada\":\""
                + fechaHoraSalidaLlegada + "\", \"TipoEstacion\":\"" + TipoEstacion + "\", \"distanciaRecorrida\":\""
                + distanciaRecorrida + "\"}";
    }
}
