package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**GuiasIdentificacion<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=GuiasIdentificacion<br>
 * @author esteban
 *
 */
public class GuiasIdentificacion {
    
    /**NumeroGuiaIdentificacion<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{10,30}<br>
     */
    @SerializedName(value = "NumeroGuiaIdentificacion")
    private String numeroGuiaIdentificacion = null;
    
    /**DescripGuiaIdentificacion<br>
     * Required<br>
     * Matching regular expression pattern: [^|]{1,1000}<br>
     */
    @SerializedName(value = "DescripGuiaIdentificacion")
    private String descripGuiaIdentificacion = null;
    
    /**PesoGuiaIdentificacion<br>
     * Required<br>
     */
    @SerializedName(value = "PesoGuiaIdentificacion")
    private BigDecimal pesoGuiaIdentificacion = null;

    public String getNumeroGuiaIdentificacion() {
        return numeroGuiaIdentificacion;
    }

    public void setNumeroGuiaIdentificacion(String numeroGuiaIdentificacion) {
        this.numeroGuiaIdentificacion = numeroGuiaIdentificacion;
    }

    public String getDescripGuiaIdentificacion() {
        return descripGuiaIdentificacion;
    }

    public void setDescripGuiaIdentificacion(String descripGuiaIdentificacion) {
        this.descripGuiaIdentificacion = descripGuiaIdentificacion;
    }

    public BigDecimal getPesoGuiaIdentificacion() {
        return pesoGuiaIdentificacion;
    }

    public void setPesoGuiaIdentificacion(BigDecimal pesoGuiaIdentificacion) {
        this.pesoGuiaIdentificacion = pesoGuiaIdentificacion;
    }

    @Override
    public String toString() {
        return "{\"numeroGuiaIdentificacion\":\"" + numeroGuiaIdentificacion + "\", \"descripGuiaIdentificacion\":\""
                + descripGuiaIdentificacion + "\", \"pesoGuiaIdentificacion\":\"" + pesoGuiaIdentificacion + "\"}";
    }
}
