package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**DetalleMercancia<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=DetalleMercancia<br>
 * @author esteban
 *
 */
public class DetalleMercancia {
    
    /**UnidadPesoMerc<br>
     * Required<br>
     */
    @SerializedName(value = "UnidadPesoMerc")
    private String unidadPesoMerc = null;
    
    /**PesoBruto<br>
     * Required<br>
     */
    @SerializedName(value = "PesoBruto")
    private BigDecimal pesoBruto = null;
    
    /**PesoNeto<br>
     * Required<br>
     */
    @SerializedName(value = "PesoNeto")
    private BigDecimal pesoNeto = null;
    
    /**PesoTara<br>
     * Required<br>
     */
    @SerializedName(value = "PesoTara")
    private BigDecimal pesoTara = null;
    
    /**NumPiezas
     * 
     */
    @SerializedName(value = "NumPiezas")
    private Integer numPiezas = null;

    public String getUnidadPesoMerc() {
        return unidadPesoMerc;
    }

    public void setUnidadPesoMerc(String unidadPesoMerc) {
        this.unidadPesoMerc = unidadPesoMerc;
    }

    public BigDecimal getPesoBruto() {
        return pesoBruto;
    }

    public void setPesoBruto(BigDecimal pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    public BigDecimal getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(BigDecimal pesoNeto) {
        this.pesoNeto = pesoNeto;
    }

    public BigDecimal getPesoTara() {
        return pesoTara;
    }

    public void setPesoTara(BigDecimal pesoTara) {
        this.pesoTara = pesoTara;
    }

    public Integer getNumPiezas() {
        return numPiezas;
    }

    public void setNumPiezas(Integer numPiezas) {
        this.numPiezas = numPiezas;
    }

    @Override
    public String toString() {
        return "{\"unidadPesoMerc\":\"" + unidadPesoMerc + "\", \"pesoBruto\":\"" + pesoBruto + "\", \"pesoNeto\":\""
                + pesoNeto + "\", \"pesoTara\":\"" + pesoTara + "\", \"numPiezas\":\"" + numPiezas + "\"}";
    }
}
