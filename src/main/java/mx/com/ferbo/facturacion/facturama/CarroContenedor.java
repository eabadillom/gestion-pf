package mx.com.ferbo.facturacion.facturama;

import java.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**CarroContenedor<br>
 * https://www.api.facturama.com.mx/docs/ResourceModel?modelName=CarroContenedor<br>
 * @author esteban
 *
 */
public class CarroContenedor {
    
    /**Required
     * 
     */
    @SerializedName(value = "TipoContenedor")
    private String tipoContenedor = null;
    
    /**Required
     */
    @SerializedName(value = "PesoContenedorVacio")
    private BigDecimal pesoContenedorVacio = null;
    
    /**Required
     */
    @SerializedName(value = "PesoNetoMercancia")
    private BigDecimal pesoNetoMercancia = null;

    public String getTipoContenedor() {
        return tipoContenedor;
    }

    public void setTipoContenedor(String tipoContenedor) {
        this.tipoContenedor = tipoContenedor;
    }

    public BigDecimal getPesoContenedorVacio() {
        return pesoContenedorVacio;
    }

    public void setPesoContenedorVacio(BigDecimal pesoContenedorVacio) {
        this.pesoContenedorVacio = pesoContenedorVacio;
    }

    public BigDecimal getPesoNetoMercancia() {
        return pesoNetoMercancia;
    }

    public void setPesoNetoMercancia(BigDecimal pesoNetoMercancia) {
        this.pesoNetoMercancia = pesoNetoMercancia;
    }

    @Override
    public String toString() {
        return "{\"tipoContenedor\":\"" + tipoContenedor + "\", \"pesoContenedorVacio\":\"" + pesoContenedorVacio
                + "\", \"pesoNetoMercancia\":\"" + pesoNetoMercancia + "\"}";
    }
}
