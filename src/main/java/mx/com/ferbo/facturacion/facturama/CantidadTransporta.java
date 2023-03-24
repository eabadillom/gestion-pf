package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**CantidadTransporta<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=CantidadTransporta<br>
 * @author esteban
 *
 */
public class CantidadTransporta {
    
    /**Cantidad<br>
     * Required<br>
     */
    @SerializedName(value = "Cantidad")
    private BigDecimal cantidad = null;
    
    /**IDOrigen<br>
     * Required<br>
     * Matching regular expression pattern: OR[0-9]{6}<br>
     */
    @SerializedName(value = "IDOrigen")
    private String idOrigen = null;
    
    /**IDDestino<br>
     * Required<br>
     * Matching regular expression pattern: DE[0-9]{6}<br>
     */
    @SerializedName(value = "IDDestino")
    private String idDestino = null;
    
    @SerializedName(value = "CvesTransporte")
    private String cvesTransporte = null;

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(String idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getIdDestino() {
        return idDestino;
    }

    public void setIdDestino(String idDestino) {
        this.idDestino = idDestino;
    }

    public String getCvesTransporte() {
        return cvesTransporte;
    }

    public void setCvesTransporte(String cvesTransporte) {
        this.cvesTransporte = cvesTransporte;
    }

    @Override
    public String toString() {
        return "{\"cantidad\":\"" + cantidad + "\", \"idOrigen\":\"" + idOrigen + "\", \"idDestino\":\"" + idDestino
                + "\", \"cvesTransporte\":\"" + cvesTransporte + "\"}";
    }
}
